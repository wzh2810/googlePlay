package com.wz.googleplay;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;

import com.astuetz.PagerSlidingTabStripExtends;
import com.wz.googleplay.base.BaseFragment;
import com.wz.googleplay.factory.FragmentFactory;
import com.wz.googleplay.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.main_tabs)
    PagerSlidingTabStripExtends mMainTabs;

    @Bind(R.id.main_viewpager)
    ViewPager mMainViewpager;

    private String[] mMainTitleArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initActionBar();
        initData();
        initListener();
    }

    private void initListener() {
        mMainTabs.setOnPageChangeListener(mMyOnPageChangeListener);

        mMainViewpager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // mViewPager视图渲染完成的时候--->手动触发选中第一页
                mMyOnPageChangeListener.onPageSelected(0);
                mMainViewpager.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

    }

    MyOnPageChangeListener	mMyOnPageChangeListener	= new MyOnPageChangeListener();

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //开始触发加载的数据
            //通过Fragment的集合拿到对应的position对应的Fragment的引用-->--BaseFragment--LoadPager对象
            BaseFragment baseFragment = FragmentFactory.createFragment(position);// 肯定是从集合得到的
            if(baseFragment != null) {
                baseFragment.mLoadingPager.triggerLoadData();
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 创建adapter-->创建了Fragment-->创建了loadingPager(视图的渲染,肯定会消耗一定的时间),为ViewPager设置了一个adapter
     */
    private void initData() {
        //模拟的dataset
        mMainTitleArr = UIUtils.getStrings(R.array.main_titles);
      //  MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
        MainFragmentStatePagerAdaper adapter = new MainFragmentStatePagerAdaper(getSupportFragmentManager());
        mMainViewpager.setAdapter(adapter);

        mMainTabs.setViewPager(mMainViewpager);
    }

    private void initActionBar() {
        //1.得到actionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("GooglePlay");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher);
    }

    class MainFragmentPagerAdapter extends FragmentPagerAdapter {

        public MainFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            if(mMainTitleArr != null) {
                return mMainTitleArr.length;
            }
            return 0;
        }

        @Override
        public Fragment getItem(int position) {
            //需要我们返回一个一个的fragment
            //工厂
            Fragment fragment = FragmentFactory.createFragment(position);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mMainTitleArr[position];
        }
    }

    class MainFragmentStatePagerAdaper extends FragmentStatePagerAdapter {

        public MainFragmentStatePagerAdaper(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = FragmentFactory.createFragment(position);
            return fragment;
        }

        @Override
        public int getCount() {
            if(mMainTitleArr != null) {
                return mMainTitleArr.length;
            }
            return 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mMainTitleArr[position];
        }
    }
}
