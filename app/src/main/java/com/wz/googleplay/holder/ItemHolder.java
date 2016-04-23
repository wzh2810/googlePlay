package com.wz.googleplay.holder;



import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import android.widget.TextView;

import com.wz.googleplay.bean.ItemInfoBean;
import com.wz.googleplay.R;
import com.wz.googleplay.base.BaseHolder;
import com.wz.googleplay.config.Constants;
import com.wz.googleplay.manager.DownLoadInfo;
import com.wz.googleplay.manager.DownLoadManager;
import com.wz.googleplay.utils.CommonUtils;
import com.wz.googleplay.utils.LogUtils;
import com.wz.googleplay.utils.StringUtils;
import com.wz.googleplay.utils.UIUtils;
import com.wz.googleplay.views.ProgressView;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.sephiroth.android.library.picasso.LruCache;
import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by wz on 2016/4/17.
 *
 * @描述 1.提供视图
 * @描述 2.接收数据, 然后进行数据和视图的绑定
 */
public class ItemHolder extends BaseHolder<ItemInfoBean> implements DownLoadManager.DownLoadInfoObserver {


    @Bind(R.id.item_appinfo_iv_icon)
    ImageView mItemAppinfoIvIcon;

    @Bind(R.id.item_appinfo_tv_title)
    TextView mItemAppinfoTvTitle;

    @Bind(R.id.item_appinfo_rb_stars)
    RatingBar mItemAppinfoRbStars;

    @Bind(R.id.item_appinfo_tv_size)
    TextView mItemAppinfoTvSize;

    @Bind(R.id.item_appinfo_tv_des)
    TextView mItemAppinfoTvDes;

    @Bind(R.id.item_appinfo_progressview)
    ProgressView mProgressView;
    public ItemInfoBean	mData;
    /**
     * 初始化持有的视图
     *
     * @return
     */
    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_home_info, null);
        //初始化孩子对象
        ButterKnife.bind(this,view);

        return view;
    }

    @Override
    public void refreshHolderView(ItemInfoBean data) {

        //重置progress
        mProgressView.setProgress(0);
        mData = data;

        mItemAppinfoTvDes.setText(data.des);
        mItemAppinfoTvSize.setText(StringUtils.formatFileSize(data.size));
        mItemAppinfoTvTitle.setText(data.name);

        mItemAppinfoRbStars.setRating(data.stars);

        //图片加载
        String picUrl = Constants.URLS.IMGBASEURL + data.iconUrl;
        Picasso.with(UIUtils.getContext())//
                .load(picUrl)//
                .fade(300)//淡入淡出的效果
                .withDiskCache(new LruCache(10 * 1024 * 1024))//可能有问题,因为Lrucache只是做内存缓存的.
                // 可能Picasso只是暴露了一个方法没有具体帮我们实现磁盘缓存
                .withCache(new LruCache(4 * 1024 * 1024))
                .into(mItemAppinfoIvIcon);

        	/*--------------- 根据不同的状态,展示不同的ui ---------------*/
        //得到状态-->得到downLoadInfo
        //谁可以提供DownLoadInfo
        DownLoadInfo downLoadInfo = DownLoadManager.getInstance().getDownLoadInfo(data);
        refreshProgressViewUi(downLoadInfo);
    }

    private void refreshProgressViewUi(DownLoadInfo downLoadInfo) {
        int state = downLoadInfo.state;
        /**
         状态(编程记录)  	    |  给用户的提示(ui展现)
         ----------------   |-----------------------
         未下载			    |下载
         下载中			    |显示进度条
         暂停下载			|继续下载
         等待下载			|等待中...
         下载失败 		    |重试
         下载完成 		    |安装
         已安装 			    |打开
         */
        switch (state) {
            case DownLoadManager.STATE_UNDOWNLOAD://未下载
                mProgressView.setNote("下载");
                mProgressView.setIcon(R.drawable.ic_download);
                break;
            case DownLoadManager.STATE_DOWNLOADING://下载中

                mProgressView.setIcon(R.drawable.ic_pause);
                mProgressView.setMax(downLoadInfo.max);
                mProgressView.setProgress(downLoadInfo.progress);

                LogUtils.i("###" + downLoadInfo.progress);

                int progress = (int) (downLoadInfo.progress * 100.f / downLoadInfo.max + 0.5f);
                mProgressView.setNote(progress + "%");
                break;
            case DownLoadManager.STATE_PAUSEDOWNLOAD: //暂停下载
                mProgressView.setIcon(R.drawable.ic_resume);
                mProgressView.setNote("继续");
                break;
            case DownLoadManager.STATE_WAITINGDOWNLOAD: //等待下载
                mProgressView.setIcon(R.drawable.ic_pause);
                mProgressView.setNote("取消");
                break;
            case DownLoadManager.STATE_DOWNLOADFAILED: //下载失败
                mProgressView.setIcon(R.drawable.ic_redownload);
                mProgressView.setNote("重试");
                break;
            case DownLoadManager.STATE_DOWNLOADED: //下载完成
                mProgressView.setIcon(R.drawable.ic_install);
                mProgressView.setNote("安装");
                mProgressView.setIsProgressEnable(false);
                break;
            case DownLoadManager.STATE_INSTALLED:// 已安装
                mProgressView.setIcon(R.drawable.ic_install);
                mProgressView.setNote("打开");
                break;
            default:
                break;

        }
    }

    @OnClick(R.id.item_appinfo_progressview)
    public void clickProgressView(View v) {
        /*--------------- 根据不同的状态,触发不同的操作 ---------------*/
		/*--------------- 3.根据不同的状态触发不同的操作 ---------------*/
        // 得到状态-->得到downLoadInfo
        // 谁可以提供DownLoadInfo

        DownLoadInfo downLoadInfo = DownLoadManager.getInstance().getDownLoadInfo(mData);
        int state = downLoadInfo.state;
        /**
         状态(编程记录)   | 用户行为(触发操作)
         ---------------| -----------------
         未下载			| 去下载
         下载中			| 暂停下载
         暂停下载	    | 断点继续下载
         等待下载		| 取消下载
         下载失败 		| 重试下载
         下载完成 		| 安装应用
         已安装 			| 打开应用
         */
        switch (state) {
            case DownLoadManager.STATE_UNDOWNLOAD:// 未下载
                doDownLoad(downLoadInfo);
                break;
            case DownLoadManager.STATE_DOWNLOADING:// 下载中
                pauseDownLoad(downLoadInfo);
                break;
            case DownLoadManager.STATE_PAUSEDOWNLOAD:// 暂停下载
                doDownLoad(downLoadInfo);// 继续断点下载
                break;
            case DownLoadManager.STATE_WAITINGDOWNLOAD:// 等待下载
                cancelDownLoad(downLoadInfo);
                break;
            case DownLoadManager.STATE_DOWNLOADFAILED:// 下载失败
                doDownLoad(downLoadInfo);
                break;
            case DownLoadManager.STATE_DOWNLOADED:// 下载完成
                installApk(downLoadInfo);
                break;
            case DownLoadManager.STATE_INSTALLED:// 已安装
                openApk(downLoadInfo);
                break;

            default:
                break;
        }
    }

    /**
     * 打开apk
     *
     * @param downLoadInfo
     */
    private void openApk(DownLoadInfo downLoadInfo) {
        CommonUtils.openApp(UIUtils.getContext(), downLoadInfo.packageName);
    }

    /**
     * 安装apk
     *
     * @param downLoadInfo
     */
    private void installApk(DownLoadInfo downLoadInfo) {
        File apkFile = new File(downLoadInfo.savePath);
        CommonUtils.installApp(UIUtils.getContext(), apkFile);
    }

    /**
     * 开始下载,继续下载,重试下载
     *
     * @param downLoadInfo
     */
    private void doDownLoad(DownLoadInfo downLoadInfo) {

		/*  // 异步 下载 apk
		  DownLoadInfo downLoadInfo = new DownLoadInfo();
		  // http://localhost:8080/GooglePlayServer/download?name=app/com.itheima.www/com.itheima.www.apk&range=0

		  Map<String, Object> reqMap = new HashMap<>();
		  reqMap.put("name", mData.downloadUrl);
		  reqMap.put("range", "0");
		  String urlParamsByMap = HttpUtil.getUrlParamsByMap(reqMap);

		  downLoadInfo.downLoadUrl = Constants.URLS.DOWNLOADBASEURL + urlParamsByMap;
		  String dir = FileUtils.getDir("apk");// sdcard/Android/data/包名/apk
		  String name = mData.packageName + ".apk";

		  File saveApkFile = new File(dir, name);

		  downLoadInfo.savePath = saveApkFile.getAbsolutePath();
		  downLoadInfo.packageName = mData.packageName;*/

        DownLoadManager.getInstance().downLoad(downLoadInfo);
    }

    /**
     * 取消下载
     *
     * @param downLoadInfo
     */
    private void cancelDownLoad(DownLoadInfo downLoadInfo) {

    }

    /**
     * 暂停下载
     *
     * @param downLoadInfo
     */
    private void pauseDownLoad(DownLoadInfo downLoadInfo) {
        DownLoadManager.getInstance().pauseDownLoad(downLoadInfo);
    }

    /*--------------- 接收downLoadInfo的改变,实时的更新ui ---------------*/
    @Override
    public void onDownLoadInfoChanged(final DownLoadInfo downLoadInfo) {
        // 过滤DownLoadInfo信息
        if (!downLoadInfo.packageName.equals(mData.packageName)) {
            return;
        }

        UIUtils.postTaskSafely(new Runnable() {
            @Override
            public void run() {
                LogUtils.i(downLoadInfo.progress + "");
                refreshProgressViewUi(downLoadInfo);
            }
        });
    }


}
