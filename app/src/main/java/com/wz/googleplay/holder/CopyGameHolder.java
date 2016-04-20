package com.wz.googleplay.holder;


import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.wz.googleplay.R;
import com.wz.googleplay.base.BaseHolder;
import com.wz.googleplay.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wz on 2016/4/17.
 *
 * @描述 1.提供视图
 * @描述 2.接收数据, 然后进行数据和视图的绑定
 */
public class CopyGameHolder extends BaseHolder<String> {


    @Bind(R.id.tv_1)
    TextView mTv_1;
    @Bind(R.id.tv_2)
    TextView mTv_2;

    /**
     * 初始化持有的视图
     *
     * @return
     */
    @Override
    public View initHolderView() {
        mRootView = View.inflate(UIUtils.getContext(), R.layout.item_temp, null);
        //初始化孩子对象
        ButterKnife.bind(this,mRootView);

        return mRootView;
    }

    /**
     * 进行数据和视图的绑定
     * @param data
     */
    @Override
    public void refreshHolderView(String data) {
        //data-->局部变量
        //view-->成员变量
        mTv_1.setText("我是头---" + data);
        mTv_1.setTextColor(Color.RED);
        mTv_2.setText("我是尾---" + data);
        mTv_2.setTextColor(Color.GREEN);
    }
}
