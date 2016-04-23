package com.wz.googleplay.holder;

import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.wz.googleplay.bean.ItemInfoBean;
import com.wz.googleplay.R;
import com.wz.googleplay.base.BaseHolder;
import com.wz.googleplay.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wz on 2016/4/21.
 */
public class DetailDesHolder extends BaseHolder<ItemInfoBean> implements View.OnClickListener {

    @Bind(R.id.app_detail_des_tv_des)
    TextView mAppDetailDesTvDes;

    @Bind(R.id.app_detail_des_tv_author)
    TextView mAppDetailDesTvAuthor;

    @Bind(R.id.app_detail_des_iv_arrow)
    ImageView mAppDetailDesIvArrow;

    private boolean isOpen = true;
    private int mDetailDesmeasuredHeight;
    private ItemInfoBean mData;

    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_detail_des, null);
        ButterKnife.bind(this, view);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void refreshHolderView(ItemInfoBean data) {
        // 保存数据到成员变量
        mData = data;

        mAppDetailDesTvAuthor.setText(data.author);
        mAppDetailDesTvDes.setText(data.des);

        mAppDetailDesTvDes.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mDetailDesmeasuredHeight = mAppDetailDesTvDes.getMeasuredHeight();
                        mAppDetailDesTvDes.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                        // 默认进来是折叠,不带动画
                        toggle(false);
                    }
                });

    }

    @Override
    public void onClick(View v) {
        toggle(true);
    }

    private void toggle(boolean hasAnimation) {
        if(isOpen) {
            // 折叠:mAppDetailDesTvDes高度变化 应有的高度-->7行的高度
            // mAppDetailDesTvDes.measure(0, 0);
            // int measuredHeight = mAppDetailDesTvDes.getMeasuredHeight();
            Toast.makeText(UIUtils.getContext(), mDetailDesmeasuredHeight + "", Toast.LENGTH_SHORT).show();
            int start = mDetailDesmeasuredHeight;
            int end = getShortLineHeight(mData, 7);
            if(hasAnimation) {
                doAnimation(start,end);
            } else {
                mAppDetailDesTvDes.setHeight(end);
            }
        } else {
            int start = getShortLineHeight(mData,7);
            int end = mDetailDesmeasuredHeight;

            if(hasAnimation) {
                doAnimation(start,end);
            } else {
                mAppDetailDesTvDes.setHeight(end);
            }
        }

        isOpen = !isOpen;
    }


    private void doAnimation(int start,int end) {
        ObjectAnimator animator = ObjectAnimator.ofInt(mAppDetailDesTvDes,"height",start,end);
        animator.start();

        if(isOpen) {
            ObjectAnimator.ofFloat(mAppDetailDesIvArrow,"rotation",0,180).start();
        } else {
            ObjectAnimator.ofFloat(mAppDetailDesIvArrow,"rotation",180,0).start();
        }

        // 监听-->监听动画的执行过程(开始,取消,结束)
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {  //动画开始

            }

            @Override
            public void onAnimationEnd(Animator animator) { //动画结束
                ViewParent parent = mAppDetailDesTvDes.getParent();
                while (true) {
                    parent = parent.getParent();
                    if(parent instanceof ScrollView) {
                        ((ScrollView) parent).fullScroll(View.FOCUS_DOWN);
                        break;
                    }
                    if(parent == null) {
                        break;
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {// 动画的取消

            }

            @Override
            public void onAnimationRepeat(Animator animator) { // 动画的重复


            }
        });
    }

    /**
     *
     * @param data 假如展示这么多数据
     * @param line 假如这么高
     * @return
     */
    private int getShortLineHeight(ItemInfoBean data,int line) {
        TextView tempTextView = new TextView(UIUtils.getContext());
        tempTextView.setText(data.date);
        tempTextView.setLines(line);
        //去测量
        tempTextView.measure(0,0);
        return tempTextView.getMeasuredHeight();
    }
}
