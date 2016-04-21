package com.wz.googleplay.holder;



import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.wz.googleplay.Bean.ItemInfoBean;
import com.wz.googleplay.R;
import com.wz.googleplay.base.BaseHolder;
import com.wz.googleplay.config.Constants;
import com.wz.googleplay.utils.PicassoUtils;
import com.wz.googleplay.utils.UIUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wz on 2016/4/21.
 */
public class DetailSafeHolder extends BaseHolder<ItemInfoBean> implements View.OnClickListener {

    @Bind(R.id.app_detail_safe_iv_arrow)
    ImageView mAppDetailSafeIvArrow;

    @Bind(R.id.app_detail_safe_pic_container)
    LinearLayout mAppDetailSafePicContainer;

    @Bind(R.id.app_detail_safe_des_container)
    LinearLayout mAppDetailSafeDesContainer;

    private boolean	isOpen	= true;


    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_detail_safe, null);
        ButterKnife.bind(this,view);

        //设置点击事件
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void refreshHolderView(ItemInfoBean data) {
        List<ItemInfoBean.ItemSafeBean> safeBeans = data.safe;
        for(ItemInfoBean.ItemSafeBean safeBean : safeBeans) {
            String safeUrl = safeBean.safeUrl;

            ImageView ivIcon = new ImageView(UIUtils.getContext());
            //图片加载
            PicassoUtils.display(Constants.URLS.IMGBASEURL + safeUrl,ivIcon);
            mAppDetailSafePicContainer.addView(ivIcon);

            String safeDes =  safeBean.safeDes;
            String safeDesUrl =  safeBean.safeDesUrl;
            int safeColor = safeBean.safeDesColor;

            LinearLayout line = new LinearLayout(UIUtils.getContext());

            ImageView ivDesIcon = new ImageView(UIUtils.getContext());
            //图片加载
            PicassoUtils.display(Constants.URLS.IMGBASEURL + safeDesUrl,ivDesIcon);

            //设置文本
            TextView tvDes = new TextView(UIUtils.getContext());
            tvDes.setText(safeDes);

            //设置文本颜色
            if(safeColor == 0) {
                tvDes.setTextColor(UIUtils.getColor(R.color.app_detail_safe_normal));
            } else {
                tvDes.setTextColor(UIUtils.getColor(R.color.app_detail_safe_warning));
            }

            int padding = UIUtils.dp2Px(3);
            line.setPadding(padding,padding,padding,padding);
            line.addView(ivDesIcon);
            line.addView(tvDes);
            mAppDetailSafeDesContainer.addView(line);
        }

        // 进入界面,默认折叠,而且没有动画效果
        toggle(false);

    }


    @Override
    public void onClick(View v) {
        toggle(true);
    }

    private void toggle(boolean hasAnimation) {
        if(isOpen) {//折叠
            // 折叠:mAppDetailSafeDesContainer高度变化 应有的高度--->0
            mAppDetailSafeDesContainer.measure(0,0);
            int desContainMeasuredHeight = mAppDetailSafeDesContainer.getMeasuredHeight();
            int start = desContainMeasuredHeight;
            int end = 0;
            if(hasAnimation) {
                doAnimation(start,end);
            } else {
                //通过layoutParams设置高度
                ViewGroup.LayoutParams layoutParams = mAppDetailSafeDesContainer.getLayoutParams();
                layoutParams.height = end;
                //重新设置params
                mAppDetailSafeDesContainer.setLayoutParams(layoutParams);
            }

        } else {
            mAppDetailSafeDesContainer.measure(0,0);
            int desContainMeasuredHeight = mAppDetailSafeDesContainer.getMeasuredHeight();
            int start = 0;
            int end = desContainMeasuredHeight;
            if(hasAnimation) {
                doAnimation(start,end);
            } else {
                //通过layoutParams设置高度
                ViewGroup.LayoutParams layoutParams = mAppDetailSafeDesContainer.getLayoutParams();
                layoutParams.height = end;
                //重新设置params
                mAppDetailSafeDesContainer.setLayoutParams(layoutParams);
            }
        }
        isOpen = !isOpen;
    }

    private void doAnimation(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start,end);
        animator.start();
        // 添加监听-->得到动画执行过程中的渐变值
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int height = (int) valueAnimator.getAnimatedValue();
                // 通过layoutParams设置高度
                ViewGroup.LayoutParams layoutParams = mAppDetailSafeDesContainer.getLayoutParams();
                layoutParams.height = height;

                // 重新设置params
                mAppDetailSafeDesContainer.setLayoutParams(layoutParams);
            }
        });

        //箭头跟着旋转
        if(isOpen) {
            ObjectAnimator.ofFloat(mAppDetailSafeIvArrow,"rotation",180,0).start();
        } else {
            ObjectAnimator.ofFloat(mAppDetailSafeIvArrow,"rotation",0,180).start();
        }
    }
}
