package com.wz.googleplay.holder;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wz.googleplay.R;
import com.wz.googleplay.base.BaseHolder;
import com.wz.googleplay.config.Constants;
import com.wz.googleplay.utils.UIUtils;
import com.wz.googleplay.views.InnerViewPager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.sephiroth.android.library.picasso.LruCache;
import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by wz on 2016/4/19.
 */


public class HomePictureHolder extends BaseHolder<List<String>> {

    @Bind(R.id.item_home_picture_pager)
    InnerViewPager mItemHomePicturePager;

    @Bind(R.id.item_home_picture_container_indicator)
    LinearLayout mItemHomePictureContainerIndicator;
    private List<String>   mPicsturesUrl;
    private AutoScrollTask mAutoScrollTask;

    @Override
    public View initHolderView() {// 视图是啥
        View view = View.inflate(UIUtils.getContext(), R.layout.item_home_pictures, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void refreshHolderView(List<String> picsturesUrl) {// 数据绑定
        // 保存数据到成员变量
        mPicsturesUrl = picsturesUrl;

        // data
        // view
        // data+view
        mItemHomePicturePager.setAdapter(new PicturesPagerAdapter());

        // 添加indicator
        for (int i = 0; i < mPicsturesUrl.size(); i++) {
            ImageView ivIndicator = new ImageView(UIUtils.getContext());
            ivIndicator.setImageResource(R.drawable.indicator_normal);

            if (i == 0) {
                ivIndicator.setImageResource(R.drawable.indicator_selected);
            }
            int width = UIUtils.dp2Px(6);// px
            int height = UIUtils.dp2Px(6);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            params.leftMargin = UIUtils.dp2Px(6);
            params.bottomMargin = UIUtils.dp2Px(6);
            mItemHomePictureContainerIndicator.addView(ivIndicator, params);
        }
        // 滑动的时候切换indicator的选中效果

        mItemHomePicturePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                position = position % mPicsturesUrl.size();

                // 1.还原
                for (int i = 0; i < mPicsturesUrl.size(); i++) {
                    ImageView ivIndicaotr = (ImageView) mItemHomePictureContainerIndicator.getChildAt(i);
                    ivIndicaotr.setImageResource(R.drawable.indicator_normal);
                    // 2.设置选中
                    if (i == position) {
                        ivIndicaotr.setImageResource(R.drawable.indicator_selected);
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 无限轮播
        // 设置初始化索引为条目总数的一半
        int diff = Integer.MAX_VALUE / 2 % mPicsturesUrl.size();
        mItemHomePicturePager.setCurrentItem(Integer.MAX_VALUE / 2 - diff);

        // 自动轮播
        if (mAutoScrollTask == null) {
            mAutoScrollTask = new AutoScrollTask();
            mAutoScrollTask.start();
        }
        //按下去的时候停止轮播
        mItemHomePicturePager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mAutoScrollTask.stop();
                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mAutoScrollTask.start();
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
    }

    class AutoScrollTask implements Runnable {
        // 开始滚动
        public void start() {
            UIUtils.getMainThreadHandler().postDelayed(this, 3000);
        }

        // 结束滚动
        public void stop() {
            UIUtils.getMainThreadHandler().removeCallbacks(this);
        }

        @Override
        public void run() {
            int curItem = mItemHomePicturePager.getCurrentItem();
            curItem++;
            mItemHomePicturePager.setCurrentItem(curItem);
            // 递归
            start();
        }
    }

    class PicturesPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (mPicsturesUrl != null) {
                // return mPicsturesUrl.size();
                return Integer.MAX_VALUE;
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            position = position % mPicsturesUrl.size();

            // data
            String url = mPicsturesUrl.get(position);

            ImageView iv = new ImageView(UIUtils.getContext());
            iv.setScaleType(ImageView.ScaleType.FIT_XY);

            // 加载图片
            String picUrl = Constants.URLS.IMGBASEURL + url;
            Picasso.with(UIUtils.getContext()).load(picUrl).withCache(new LruCache(4 * 1024 * 1024)).into(iv);

            container.addView(iv);

            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}


//public class HomePictureHolder extends BaseHolder<List<String>> {
//    @Bind(R.id.item_home_picture_pager)
//    InnerViewPager mItemHomePicturePager;
//
//    @Bind(R.id.item_home_picture_container_indicator)
//    LinearLayout mItemHomePictureContainerIndicator;
//
//    private List<String> mPicsturesUrl;
//
//    private View mView;
//
//
//
//    @Override
//    public View initHolderView() {  // 视图是啥
//        View view = View.inflate(UIUtils.getContext(), R.layout.item_home_pictures, null);
//        ButterKnife.bind(this,view);
//
//        mView = view.findViewById(R.id.item_home_picture_pager);
//        mView.
//
//        return view;
//    }
//
//    @Override
//    public void refreshHolderView(List<String> picsturesUrl) { //数据绑定
//       mPicsturesUrl = picsturesUrl;
//
//        mItemHomePicturePager.setAdapter(new PicturesPagerAdapter());
//
//        //添加indicator
//        for (int  i = 0; i < mPicsturesUrl.size(); i ++) {
//            ImageView ivIndicator = new ImageView(UIUtils.getContext());
//            ivIndicator.setImageResource(R.drawable.indicator_normal);
//
//            if(i == 0) {
//                ivIndicator.setImageResource(R.drawable.indicator_selected);
//            }
//            int width = UIUtils.dp2Px(6);
//            int height = UIUtils.dp2Px(6);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
//            params.leftMargin = UIUtils.dp2Px(6);
//            params.bottomMargin = UIUtils.dp2Px(6);
//            mItemHomePictureContainerIndicator.addView(ivIndicator, params);
//        }
//    }
//
//    //滑动的时候切换indicator的选中效果
////    mItemHomePicturePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
////
////    });
//
//
//    // 无限轮播
//    // 设置初始化索引为条目总数的一半
//    int diff = Integer.MAX_VALUE / 2 % mPicsturesUrl.size();
//    mItemHomePicturePager.setCurrentItem(Integer.MAX_VALUE / 2 - diff);
//
//
//    class HomeOnPageChangeListener implements OnPageChangeListener {
//
//        @Override
//        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//        }
//
//        @Override
//        public void onPageSelected(int position) {
//
//            position = position % mPicsturesUrl.size();
//
//            // 1.还原
//            for (int i = 0; i < mPicsturesUrl.size(); i++) {
//                ImageView ivIndicaotr = (ImageView) mItemHomePictureContainerIndicator.getChildAt(i);
//                ivIndicaotr.setImageResource(R.drawable.indicator_normal);
//                // 2.设置选中
//                if (i == position) {
//                    ivIndicaotr.setImageResource(R.drawable.indicator_selected);
//                }
//            }
//
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int state) {
//
//        }
//    }
//
//
//    public InnerViewPager getmItemHomePicturePager() {
//        return mItemHomePicturePager;
//    }
//
//    // 无限轮播
//    // 设置初始化索引为条目总数的一半
//    int diff = Integer.MAX_VALUE / 2 % mPicsturesUrl.size();
//
//    class PicturesPagerAdapter extends PagerAdapter {
//
//        @Override
//        public int getCount() {
//            if(mPicsturesUrl != null) {
//                return mPicsturesUrl.size();
//            }
//            return 0;
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == object;
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            // data
//            String url = mPicsturesUrl.get(position);
//
//            ImageView iv = new ImageView(UIUtils.getContext());
//            iv.setScaleType(ImageView.ScaleType.FIT_XY);
//
//            //加载图片
//            String picUrl = Constants.URLS.IMGBASEURL + url;
//            Picasso.with(UIUtils.getContext()).load(picUrl).withCache(new LruCache(4* 1024 * 1024)).into(iv);
//            container.addView(iv);
//
//            return iv;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView((View)object);
//        }
//    }
//}