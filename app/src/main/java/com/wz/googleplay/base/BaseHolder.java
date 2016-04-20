package com.wz.googleplay.base;

import android.view.View;

/**
 * Created by wz on 2016/4/17.
 */
public abstract class BaseHolder<HOLDERBEANTYPE> {
    public View mRootView; //持有的视图
    private HOLDERBEANTYPE mData;

    public BaseHolder() {
        mRootView = initHolderView();
        // 找一个holder,然后把它绑定到convertView身上
        mRootView.setTag(this);
    }

    /**
     * 接收数据，然后进行数据和视图的绑定
     *
     */
    public void setDataAndRefreshHolderView(HOLDERBEANTYPE data) {
        //保存传递过来的数据到成员变量
        mData = data;
        refreshHolderView(data);
    }

    /*############### 定义两个抽象方法 ###############*/

    /**
     * 初始化持有的视图
     * 在BaseHolder里面不知道如何具体的初始化持有的视图
     * 必须实现但是不知道具体实现，定义为抽象方法，交个子类具体实现
     *
     */
    public abstract View initHolderView() ;

    /**
     * 进行数据和视图的绑定
     * 在BaseHolder里面不知道如何具体的初始化持有的视图
     * 必须实现但是不知道具体实现,定义成为抽象方法,交给子类具体实现
     * @param data
     */
    public abstract  void refreshHolderView(HOLDERBEANTYPE data);


}
