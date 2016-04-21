package com.wz.googleplay.fragment;

import android.graphics.Color;
import android.graphics.RadialGradient;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.wz.googleplay.base.BaseFragment;
import com.wz.googleplay.base.LoadingPager;
import com.wz.googleplay.protocol.HotProtocol;
import com.wz.googleplay.utils.UIUtils;
import com.wz.googleplay.views.FlowLayout;

import java.util.List;
import java.util.Random;

/**
 * Created by wz on 2016/4/16.
 */
public class HotFragment extends BaseFragment {
    private List<String> mDatas;

    /**
     * @return
     * @des 真正的在子线程里面开始加载想加载的数据
     * @call 想要加载数据的时候
     * @return 返回值只能是(1,2 ,3) 中的某一个值,如果设置为其他值,会影响视图的显示逻辑
     */
    @Override
    protected LoadingPager.LoadedResult initData() {
        HotProtocol protocol = new HotProtocol();
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
        ScrollView sv = new ScrollView(UIUtils.getContext());
        FlowLayout fl = new FlowLayout(UIUtils.getContext());
        for (final String data : mDatas) {
            TextView tv = new TextView(UIUtils.getContext());
            tv.setGravity(Gravity.CENTER);
            int padding = UIUtils.dp2Px(4);
            tv.setPadding(padding,padding,padding,padding);
            tv.setTextColor(Color.WHITE);

            // 随机颜色
            Random random = new Random();
            int alpha = 255;
            int red = random.nextInt(180) + 30;// 30-210
            int green = random.nextInt(180) + 30;// 30-210
            int blue = random.nextInt(180) + 30;// 30-210
            int argb = Color.argb(alpha, red, green, blue);
            GradientDrawable normalBg = new GradientDrawable();
            normalBg.setColor(argb);
            normalBg.setCornerRadius(10);

            GradientDrawable pressedBg = new GradientDrawable();
            pressedBg.setCornerRadius(10);
            pressedBg.setColor(Color.GRAY);


            StateListDrawable selectorBg = new StateListDrawable();
            selectorBg.addState(new int[] { android.R.attr.state_pressed }, pressedBg);
            selectorBg.addState(new int[] {}, normalBg);

            tv.setClickable(true);

            // 设置带有按下状态的drawable
            tv.setBackgroundDrawable(selectorBg);

            tv.setText(data);

            fl.addView(tv);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UIUtils.getContext(), data, Toast.LENGTH_SHORT).show();
                }
            });

        }

        sv.addView(fl);
        return sv;
    }
}
