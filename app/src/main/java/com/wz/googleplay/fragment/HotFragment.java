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
import com.wz.googleplay.utils.UIUtils;

import java.util.Random;

/**
 * Created by wz on 2016/4/16.
 */
public class HotFragment extends BaseFragment {

    /**
     * @return
     * @des 真正的在子线程里面开始加载想加载的数据
     * @call 想要加载数据的时候
     * @return 返回值只能是(1,2 ,3) 中的某一个值,如果设置为其他值,会影响视图的显示逻辑
     */
    @Override
    protected LoadingPager.LoadedResult initData() {
        SystemClock.sleep(2000);
        /**

         public static final int	STATE_EMPTY		= 1;				// 空视图
         public static final int	STATE_ERROR		= 2;				// 错误视图
         public static final int	STATE_SUCCESS	= 3;				// 成功视图
         */
        LoadingPager.LoadedResult[] loadedResultArr =
                { LoadingPager.LoadedResult.EMPTY, LoadingPager.LoadedResult.ERROR, LoadingPager.LoadedResult.SUCCESS };
        Random random = new Random();


        return loadedResultArr[random.nextInt(loadedResultArr.length)];// 3--> 0 1 2
    }

    @Override
    protected View initSuccessView() {
        TextView tv = new TextView(UIUtils.getContext()) ;
        tv.setGravity(Gravity.CENTER);
        tv.setText(this.getClass().getSimpleName());
        tv.setTextColor(Color.RED);
        return tv;
    }
}
