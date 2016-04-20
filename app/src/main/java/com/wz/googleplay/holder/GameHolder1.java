package com.wz.googleplay.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.wz.googleplay.R;
import com.wz.googleplay.utils.UIUtils;

/**
 * Created by wz on 2016/4/17.
 * @描述	      1.提供视图
 * @描述	      2.接收数据,然后进行数据和视图的绑定
 */
public class GameHolder1 {
    public View mRootView;
    public TextView mTv_1;
    public TextView mTv_2;
    private String mData;

    public GameHolder1() {
        mRootView = initHolderVIew();
    }

    /**
     * 初始化持有的视图
     * @return
     */
    private View initHolderVIew() {
        mRootView = View.inflate(UIUtils.getContext(), R.layout.item_temp,null);

        //找出孩子对象
        mTv_1 = (TextView) mRootView.findViewById(R.id.tv_1);
        mTv_2 = (TextView) mRootView.findViewById(R.id.tv_2);

        //找到一个holder，然后把它绑定到convertView身上
        mRootView.setTag(this);
        return  mRootView;
    }

    /**
     * 接收数据，然后进行数据和视图的绑定
     */
    public void setDataAndRefreshHolderView(String data) {
        //保存传递过来的数据到成员变量
        mData = data;
        refreshHolderView(data);
    }

    /**
     * 进行数据和视图的绑定
     * @param data
     */
    private void refreshHolderView(String data) {
        // data-->局部变量,成员变量也有
        // view-->成员变量
        // data+view
        mTv_1.setText("我是头---" + data);
        mTv_1.setTextColor(Color.RED);
        mTv_2.setText("我是尾---" + data);
        mTv_2.setTextColor(Color.GREEN);
    }
}
