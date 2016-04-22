package com.wz.googleplay.holder;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wz.googleplay.Bean.ItemInfoBean;
import com.wz.googleplay.R;
import com.wz.googleplay.base.BaseHolder;
import com.wz.googleplay.config.Constants;
import com.wz.googleplay.manager.DownLoadInfo;
import com.wz.googleplay.manager.DownLoadManager;
import com.wz.googleplay.utils.CommonUtils;
import com.wz.googleplay.utils.FileUtils;
import com.wz.googleplay.utils.HttpUtil;
import com.wz.googleplay.utils.LogUtils;
import com.wz.googleplay.utils.UIUtils;
import com.wz.googleplay.views.ProgressButton;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wz on 2016/4/21.
 */
public class DetailDownLoadHolder extends BaseHolder<ItemInfoBean> implements DownLoadManager.DownLoadInfoObserver {

    @Bind(R.id.app_detail_download_btn_favo)
    Button					mAppDetailDownloadBtnFavo;

    @Bind(R.id.app_detail_download_btn_share)
    Button					mAppDetailDownloadBtnShare;

    @Bind(R.id.app_detail_download_btn_download)
    ProgressButton			mAppDetailProgressBtn;
    private ItemInfoBean	mData;

    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_detail_download, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void refreshHolderView(ItemInfoBean data) {
        mData = data;
		/*--------------- 2.根据不同的状态给用户提示 ---------------*/
        // 得到状态-->得到downLoadInfo
        // 谁可以提供DownLoadInfo
        DownLoadInfo downLoadInfo = DownLoadManager.getInstance().getDownLoadInfo(data);
        refreshProgressBtnUi(downLoadInfo);
    }

    private void refreshProgressBtnUi(DownLoadInfo downLoadInfo) {
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

        mAppDetailProgressBtn.setBackgroundResource(R.drawable.selector_app_detail_bottom_normal);
        switch (state) {
            case DownLoadManager.STATE_UNDOWNLOAD:// 未下载
                mAppDetailProgressBtn.setText("下载");
                break;
            case DownLoadManager.STATE_DOWNLOADING:// 下载中

                mAppDetailProgressBtn.setMax(downLoadInfo.max);
                mAppDetailProgressBtn.setProgress(downLoadInfo.progress);

                int progress = (int) (downLoadInfo.progress * 100.f / downLoadInfo.max + .5f);

                mAppDetailProgressBtn.setText(progress + "%");

                mAppDetailProgressBtn.setBackgroundResource(R.drawable.selector_app_detail_bottom_downloading);
                break;
            case DownLoadManager.STATE_PAUSEDOWNLOAD:// 暂停下载
                mAppDetailProgressBtn.setText("继续");
                break;
            case DownLoadManager.STATE_WAITINGDOWNLOAD:// 等待下载
                mAppDetailProgressBtn.setText("取消");
                break;
            case DownLoadManager.STATE_DOWNLOADFAILED:// 下载失败
                mAppDetailProgressBtn.setText("重试");
                break;
            case DownLoadManager.STATE_DOWNLOADED:// 下载完成
                mAppDetailProgressBtn.setText("安装");
                mAppDetailProgressBtn.setIsProgressEnable(false);
                break;
            case DownLoadManager.STATE_INSTALLED:// 已安装
                mAppDetailProgressBtn.setText("打开");
                break;

            default:
                break;
        }
    }

    @OnClick(R.id.app_detail_download_btn_download)
    public void download(View view) {
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

    /*--------------- 收到发布消息,做ui更新 ---------------*/
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
                refreshProgressBtnUi(downLoadInfo);
            }
        });
    }
}

