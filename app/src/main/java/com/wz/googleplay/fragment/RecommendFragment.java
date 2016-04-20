package com.wz.googleplay.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wz.googleplay.base.BaseFragment;
import com.wz.googleplay.base.LoadingPager;
import com.wz.googleplay.protocol.RecommendProtocol;
import com.wz.googleplay.utils.UIUtils;
import com.wz.googleplay.views.flyinout.ShakeListener;
import com.wz.googleplay.views.flyinout.StellarMap;

import java.util.List;
import java.util.Random;

/**
 * Created by wz on 2016/4/16.
 */
public class RecommendFragment extends BaseFragment {

    private List<String> mDatas;
    private ShakeListener mShakeListener;

    /**
     * @return 返回值只能是(1, 2, 3) 中的某一个值,如果设置为其他值,会影响视图的显示逻辑
     * @des 真正的在子线程里面开始加载想加载的数据
     * @call 想要加载数据的时候
     */
    @Override
    protected LoadingPager.LoadedResult initData() {
        RecommendProtocol protocol = new RecommendProtocol();
        try {
            mDatas = protocol.loadData(0);
            return checkLoadedResult(mDatas);
        } catch (Exception e) {
            e.printStackTrace();
            return LoadingPager.LoadedResult.ERROR;
        }
    }

    /**
     * @return
     * @des 展示具体的成功视图
     * @call 数据加载完成, 并且是数据加载成功的时候
     */
    @Override
    protected View initSuccessView() {

        final StellarMap stellarMap = new StellarMap(UIUtils.getContext());

        // 绑定数据
        final RecommendAdapter adapter = new RecommendAdapter();
        stellarMap.setAdapter(adapter);

        //设置默认显示第一页的数据
        stellarMap.setGroup(0,true);

        //按照我们自己的规则拆分屏幕
        stellarMap.setRegularity(15,20);

        //摇一摇进行切换
        mShakeListener = new ShakeListener(UIUtils.getContext());
        mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
            @Override
            public void onShake() {
                //进行切换
                int curItem = stellarMap.getCurrentGroup();
                if (curItem == adapter.getGroupCount() - 1) {
                    curItem = 0;
                } else {
                    curItem++;
                }
                stellarMap.setGroup(curItem, true);
            }
        });
        return stellarMap;
    }

    @Override
    public void onResume() {
        //mShakeListener和Fragment的生命周期进行绑定
        if(mShakeListener!=null){
            mShakeListener.resume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        //mShakeListener和Fragment的生命周期进行绑定
        if(mShakeListener!=null){
            mShakeListener.pause();
        }
        super.onPause();
    }

    class RecommendAdapter implements StellarMap.Adapter {

        private static final int	PAGESIZE	= 15;	// 每页显示15条数据

        @Override
        public int getGroupCount() {// 得到有多少组
            if (mDatas.size() % PAGESIZE != 0) {
                return mDatas.size() / PAGESIZE + 1;// 32/15=2
            }
            return mDatas.size() / PAGESIZE;

        }

        @Override
        public int getCount(int group) {// 得到每组有多少个 32 15 15 2
            if (mDatas.size() % PAGESIZE != 0) {
                // 最后一页特殊处理
                if (group == getGroupCount() - 1) {
                    return mDatas.size() % PAGESIZE;
                }
            }
            return PAGESIZE;
        }

        @Override
        public View getView(int group, int position, View convertView) {// 返回具体视图

            // data
            int index = group * PAGESIZE + position;
            String data = mDatas.get(index);

            TextView tv = new TextView(UIUtils.getContext());
            // 随机大小
            Random random = new Random();
            tv.setTextSize(random.nextInt(6) + 12);// 12-18

            // 随机颜色
            int alpha = 255;
            int red = random.nextInt(180)+30;//30-210
            int green = random.nextInt(180)+30;
            int blue = random.nextInt(180)+30;
            int argb = Color.argb(alpha, red, green, blue);
            tv.setTextColor(argb);

            tv.setText(data);

            return tv;

        }

        @Override
        public int getNextGroupOnPan(int group, float degree) {
            return 0;
        }

        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            return 0;
        }
    }
}