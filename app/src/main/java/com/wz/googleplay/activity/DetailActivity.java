package com.wz.googleplay.activity;


import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wz.googleplay.Bean.ItemInfoBean;
import com.wz.googleplay.R;
import com.wz.googleplay.base.LoadingPager;
import com.wz.googleplay.protocol.DetailProtocol;
import com.wz.googleplay.utils.UIUtils;

public class DetailActivity extends AppCompatActivity {
    public static final String	PACKAGENAME	= "packageName";
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
                TextView tv = new TextView(UIUtils.getContext());
                tv.setText(mItemInfoBean.toString());
                return tv;
            }
        };
        setContentView(mLoadingPager);
    }

    private LoadingPager.LoadedResult onLoadData() {
        //真正的在子线程中加载数据
        DetailProtocol protocol = new DetailProtocol(mPackageName);
        try {
            mItemInfoBean = protocol.loadData(0);
            if(mItemInfoBean != null) {
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
