package com.wz.googleplay.activity;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.wz.googleplay.Bean.ItemInfoBean;
import com.wz.googleplay.R;
import com.wz.googleplay.base.LoadingPager;
import com.wz.googleplay.holder.DetailDesHolder;
import com.wz.googleplay.holder.DetailDownLoadHolder;
import com.wz.googleplay.holder.DetailInfoHolder;
import com.wz.googleplay.holder.DetailPicHolder;
import com.wz.googleplay.holder.DetailSafeHolder;
import com.wz.googleplay.protocol.DetailProtocol;
import com.wz.googleplay.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    public static final String PACKAGENAME = "packageName";

    @Bind(R.id.detail_fl_download)
    FrameLayout mDetailFlDownload;

    @Bind(R.id.detail_fl_info)
    FrameLayout mDetailFlInfo;

    @Bind(R.id.detail_fl_safe)
    FrameLayout mDetailFlSafe;

    @Bind(R.id.detail_fl_pic)
    FrameLayout mDetailFlPic;

    @Bind(R.id.detail_fl_des)
    FrameLayout mDetailFlDes;

    private String mPackageName;
    LoadingPager mLoadingPager;
    private ItemInfoBean mItemInfoBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        initActionBar();
        initView();
        initData();


    }

    private void initView() {
        mLoadingPager = new LoadingPager(UIUtils.getContext()) {
            @Override
            public LoadedResult initData() {
                return DetailActivity.this.onLoadData();
            }

            @Override
            public View initSuccessView() {
                View view = View.inflate(UIUtils.getContext(), R.layout.item_detailactivity, null);
                ButterKnife.bind(DetailActivity.this,view);
                // 就是要填充具体的内容

                // 应用的信息部分
                DetailInfoHolder detailInfoHolder = new DetailInfoHolder();
                mDetailFlInfo.addView(detailInfoHolder.mRootView);
                detailInfoHolder.setDataAndRefreshHolderView(mItemInfoBean);

                // 应用的安全部分
                DetailSafeHolder detailSafeHolder = new DetailSafeHolder();
                mDetailFlSafe.addView(detailSafeHolder.mRootView);
                detailSafeHolder.setDataAndRefreshHolderView(mItemInfoBean);

                // 应用的截图部分
                DetailPicHolder detailPicHolder = new DetailPicHolder();
                mDetailFlPic.addView(detailPicHolder.mRootView);
                detailPicHolder.setDataAndRefreshHolderView(mItemInfoBean);

                // 应用的描述部分
                DetailDesHolder detailDesHolder = new DetailDesHolder();
                mDetailFlDes.addView(detailDesHolder.mRootView);
                detailDesHolder.setDataAndRefreshHolderView(mItemInfoBean);

                // 应用的下载部分
                DetailDownLoadHolder detailDownLoadHolder = new DetailDownLoadHolder();
                mDetailFlDownload.addView(detailDownLoadHolder.mRootView);
                detailDownLoadHolder.setDataAndRefreshHolderView(mItemInfoBean);
                return view  ;
            }
        };
        setContentView(mLoadingPager);
    }

    private LoadingPager.LoadedResult onLoadData() {
        //真正的在子线程中加载数据
        DetailProtocol protocol = new DetailProtocol(mPackageName);
        try {
            mItemInfoBean = protocol.loadData(0);
            if (mItemInfoBean != null) {
                return LoadingPager.LoadedResult.SUCCESS;
            }
            return LoadingPager.LoadedResult.EMPTY;
        } catch (Exception e) {
            e.printStackTrace();
            return LoadingPager.LoadedResult.ERROR;
        }
    }

    private void initData() {
        //开始触发加载应用详情所对应的数据
        mLoadingPager.triggerLoadData();
    }


    private void init() {
        mPackageName = getIntent().getStringExtra(PACKAGENAME);
    }

    private void initActionBar() {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle("GooglePlay");
        //   supportActionBar.setDisplayShowHomeEnabled(true);
        supportActionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
