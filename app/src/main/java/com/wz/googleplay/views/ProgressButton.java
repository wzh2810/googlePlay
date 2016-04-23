package com.wz.googleplay.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;



/**
 * Created by wz on 2016/4/22.
 */
public class ProgressButton extends Button {
    private long	mMax				= 100;
    private long	mProgress;
    private boolean	isProgressEnable	= true;

    /**设置进度的最大值*/
    public void setMax(long max) {
        mMax = max;
    }

    /**设置进度的当前值*/
    public void setProgress(long progress) {
        mProgress = progress;
        // 重新绘制
        invalidate();
    }

    /**设置是否允许进度*/
    public void setIsProgressEnable(boolean isProgressEnable) {
        this.isProgressEnable = isProgressEnable;
    }

    public ProgressButton(Context context) {
        super(context);
    }

    public ProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // onMeasure onLayout onDraw

    @Override
    protected void onDraw(Canvas canvas) {

        if (isProgressEnable) {
            Drawable drawable = new ColorDrawable(Color.BLUE);

            // 指定具体drawable绘制的范围

            int left = 0;
            int top = 0;

            int right = (int) (mProgress * 1.0f / mMax * getMeasuredWidth() + .5f);// 不能写死,动态计算

            int bottom = getBottom();
            drawable.setBounds(left, top, right, bottom);
            drawable.draw(canvas);
        }

        super.onDraw(canvas);
    }
}
