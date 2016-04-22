package com.wz.googleplay.holder;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wz.googleplay.Bean.ItemInfoBean;
import com.wz.googleplay.R;
import com.wz.googleplay.base.BaseHolder;
import com.wz.googleplay.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wz on 2016/4/21.
 */
public class DetailDownLoadHolder extends BaseHolder<ItemInfoBean> {

    @Bind(R.id.app_detail_download_btn_favo)
    Button mAppDetailDownloadBtnFavo;
    @Bind(R.id.app_detail_download_btn_share)
    Button mAppDetailDownloadBtnShare;
    @Bind(R.id.app_detail_download_btn_download)
    Button mAppDetailDownloadBtnDownload;

    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_detail_download, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void refreshHolderView(ItemInfoBean data) {

    }

    @OnClick(R.id.app_detail_download_btn_download)
    public void download(View view) {
        Toast.makeText(UIUtils.getContext(),"下载",Toast.LENGTH_LONG).show();
    }
}
