package com.wz.googleplay.manager;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.wz.googleplay.Bean.ItemInfoBean;
import com.wz.googleplay.config.Constants;
import com.wz.googleplay.factory.ThreadPoolProxyFactory;
import com.wz.googleplay.utils.CommonUtils;
import com.wz.googleplay.utils.FileUtils;
import com.wz.googleplay.utils.HttpUtil;
import com.wz.googleplay.utils.IOUtils;
import com.wz.googleplay.utils.LogUtils;
import com.wz.googleplay.utils.UIUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wz on 2016/4/22.
 * @描述	      下载管理器,是应用的一个模块,处理的就是和下载相关的逻辑
 * @描述	      一个应用只需要有一个下载管理器,单例化
 * @描述	      影响下载状态的变化
 * @描述	      1.需要`时刻记录`当前的状态
 * @描述	      2.根据ItemInfoBean返回对应的DownLoadInfo信息
 *
 */
public class DownLoadManager {
    private static DownLoadManager		instance;

    public static final int				STATE_UNDOWNLOAD		= 0;					// 未下载
    public static final int				STATE_DOWNLOADING		= 1;					// 下载中
    public static final int				STATE_PAUSEDOWNLOAD		= 2;					// 暂停下载
    public static final int				STATE_WAITINGDOWNLOAD	= 3;					// 等待下载
    public static final int				STATE_DOWNLOADFAILED	= 4;					// 下载失败
    public static final int				STATE_DOWNLOADED		= 5;					// 下载完成
    public static final int				STATE_INSTALLED			= 6;					// 已安装

    // 记录用户点击下载按钮之后,传递过来的DownLoadInfo,其实就是在下载的一些任务
    private Map<String, DownLoadInfo>	mCacheDownLoadInfoMap	= new HashMap<>();
    OkHttpClient						okHttpClient			= new OkHttpClient();

    private DownLoadManager() {
        // / 1.创建okHttpCLient对象
    }

    public static DownLoadManager getInstance() {
        if (instance == null) {
            synchronized (DownLoadManager.class) {
                if (instance == null) {
                    instance = new DownLoadManager();
                }
            }
        }
        return instance;
    }

    /**
     * @des 开始异步加载apk,断点下载,暂停下载
     * @call 用户触发了下载操作(点击了下载按钮)
     * @param downLoadInfo 封装的和下载相关的参数
     */
    public void downLoad(DownLoadInfo downLoadInfo) {

        mCacheDownLoadInfoMap.put(downLoadInfo.packageName, downLoadInfo);
		/*############### 当前状态:未下载 ###############*/
        downLoadInfo.state = STATE_UNDOWNLOAD;

        // 状态发生改变,发布最新消息
        notifyObservers(downLoadInfo);
		/*#######################################*/

        /**
         什么叫等待状态?
         已经有3个任务在执行的时候
         预先把状态设置为等待中
         1.该任务加入到了"工作线程",状态其实立马会切换到-->下载中
         2.该任务加入到了"任务队列中",状态还是继续保持-->等待下载
         */
		/*############### 当前状态:等待下载 ###############*/
        downLoadInfo.state = STATE_WAITINGDOWNLOAD;

        // 状态发生改变,发布最新消息
        notifyObservers(downLoadInfo);
		/*#######################################*/

        ThreadPoolProxyFactory.createDownloadThreadPoolProxy().execute(new DownLoadTask(downLoadInfo));

    }

    /**
     * 暂停下载
     * @param downLoadInfo
     */
    public void pauseDownLoad(DownLoadInfo downLoadInfo) {
		/*############### 当前的状态: 暂停状态###############*/
        downLoadInfo.state = STATE_PAUSEDOWNLOAD;

        // 状态改变,通知观察者
        notifyObservers(downLoadInfo);
		/*#######################################*/
    }

    class DownLoadTask implements Runnable {
        private DownLoadInfo	downLoadInfo;

        public DownLoadTask(DownLoadInfo downLoadInfo) {
            this.downLoadInfo = downLoadInfo;
        }

        @Override
        public void run() {
            File apkFile = new File(downLoadInfo.savePath);
            try {

				/*############### 当前状态:下载中 ###############*/
                downLoadInfo.state = STATE_DOWNLOADING;

                // 状态发生改变,发布最新消息
                notifyObservers(downLoadInfo);
				/*#######################################*/

                // 真正的在子线程中开始加载数据

                // // 1.创建okHttpCLient对象
                // OkHttpClient okHttpClient = new OkHttpClient();

                long initRange = 0;
                if (apkFile.exists()) {
                    initRange = apkFile.length();
                }

                // ②设置进度的初始值
                downLoadInfo.progress = initRange;

                String downLoadUrl = downLoadInfo.downLoadUrl + "&range=" + initRange;// ①发起请求的处理

                LogUtils.i(downLoadUrl);
                // http://188.188.3.100:8080/GooglePlayServer/download?name=app/com.itheima.www/com.itheima.www.apk
                // 2.创建请求对象
                Request request = new Request.Builder().get().url(downLoadUrl).tag(downLoadInfo.packageName).build();

                // 3.发起请求
                Response response = okHttpClient.newCall(request).execute();

                boolean isPause = false;
                if (response.isSuccessful()) {
                    InputStream inputStream = null;
                    FileOutputStream out = null;

                    try {
                        // string inputstream
                        inputStream = response.body().byteStream();

                        out = new FileOutputStream(apkFile, true);// ③,以追加的方式去写文件

                        byte[] buffer = new byte[1024];
                        int len = -1;
                        while ((len = inputStream.read(buffer)) != -1) {

                            downLoadInfo.progress += len;

                            out.write(buffer, 0, len);
                            out.flush();

                            if (downLoadInfo.state == STATE_PAUSEDOWNLOAD) {// 用户进行了暂停操作
                                isPause = true;
                                break;
                            }

							/*############### 当前状态:下载中 ###############*/
                            downLoadInfo.state = STATE_DOWNLOADING;

                            // 状态发生改变,发布最新消息
                            notifyObservers(downLoadInfo);
							/*#######################################*/
                        }
                        if (isPause) {// 因为暂停了,所有来到了这个地方
                            LogUtils.i("###用户暂停了");

                        } else {// 下载完成后来到这个地方
                            // 下载完成

							/*############### 当前状态:下载完成 ###############*/
                            downLoadInfo.state = STATE_DOWNLOADED;

                            // 状态发生改变,发布最新消息
                            notifyObservers(downLoadInfo);
							/*#######################################*/
                            LogUtils.i("###下载完成了");
                        }

                        okHttpClient.cancel(downLoadInfo.packageName);
                    } finally {
                        IOUtils.close(out);
                        IOUtils.close(inputStream);
                    }
                } else {
					/*############### 当前状态:下载失败 ###############*/
                    downLoadInfo.state = STATE_DOWNLOADFAILED;

                    // 状态发生改变,发布最新消息
                    notifyObservers(downLoadInfo);
					/*#######################################*/
                }

            } catch (Exception e) {
                LogUtils.i("###出现异常了");
                e.printStackTrace();
				/*############### 当前状态:下载失败 ###############*/
                downLoadInfo.state = STATE_DOWNLOADFAILED;

                // 状态发生改变,发布最新消息
                notifyObservers(downLoadInfo);
				/*#######################################*/
            }
        }
    }

    /**
     * @des DetailDownLoadHolder中根据当前的数据返回一个对应的DownLoadInfo
     * @param data
     */
    public DownLoadInfo getDownLoadInfo(ItemInfoBean data) {
        DownLoadInfo downLoadInfo = new DownLoadInfo();

        // 常规赋值
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("name", data.downloadUrl);
        // reqMap.put("range", 0);//先不处理,在真正发起请求的地方,再来处理range参数
        String urlParamsByMap = HttpUtil.getUrlParamsByMap(reqMap);

        String dir = FileUtils.getDir("apk");// sdcard/Android/data/包名/apk
        String name = data.packageName + ".apk";
        File saveApkFile = new File(dir, name);

        downLoadInfo.savePath = saveApkFile.getAbsolutePath();
        // http://188.188.3.100:8080/GooglePlayServer/download?name=app/com.itheima.www/com.itheima.www.apk&range=0
        downLoadInfo.downLoadUrl = Constants.URLS.DOWNLOADBASEURL + urlParamsByMap;
        downLoadInfo.packageName = data.packageName;
        downLoadInfo.max = data.size;
        downLoadInfo.progress = 0;

        /**
         未下载

         下载中
         暂停下载
         等待下载
         下载失败


         */
        // 最主要是其中的state

        // 判断是否是 已安装
        if (CommonUtils.isInstalled(UIUtils.getContext(), data.packageName)) {
            downLoadInfo.state = STATE_INSTALLED;
            return downLoadInfo;
        }

        // 判断是否是 下载完成
        if (saveApkFile.exists()) {
            if (saveApkFile.length() == data.size) {
                downLoadInfo.state = STATE_DOWNLOADED;
                return downLoadInfo;
            }
        }

        // 判断是否 下载中,暂停下载,等待下载,下载失败
        if (mCacheDownLoadInfoMap.containsKey(data.packageName)) {
            return mCacheDownLoadInfoMap.get(data.packageName);
        }

        // 走到最后
        return downLoadInfo;// 默认就是未下载
    }

    /*--------------- 自己实现观察者设计模式,通知DownLoadInfo信息的改变  begin---------------*/
    // 1.定义观察者,以及接口方法
    public interface DownLoadInfoObserver {
        void onDownLoadInfoChanged(DownLoadInfo downLoadInfo);
    }

    // 2.定义观察者集合
    private List<DownLoadInfoObserver>	observers	= new ArrayList<DownLoadInfoObserver>();

    // 3.添加观察者
    public synchronized void addObserver(DownLoadInfoObserver o) {
        if (o == null)
            throw new NullPointerException();
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    // 4.移除观察者
    public synchronized void deleteObserver(DownLoadInfoObserver o) {
        observers.remove(o);
    }

    // 5.通知观察者数据发生改变
    public void notifyObservers(DownLoadInfo downLoadInfo) {
        for (DownLoadInfoObserver o : observers) {
            o.onDownLoadInfoChanged(downLoadInfo);
        }
    }
	/*--------------- 自己实现观察者设计模式,通知DownLoadInfo信息的改变  end---------------*/
}

