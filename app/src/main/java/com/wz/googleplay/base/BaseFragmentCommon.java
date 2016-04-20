package com.wz.googleplay.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wz on 2016/4/16.
 * @描述 Fragment的常规抽取
 */
public abstract class BaseFragmentCommon extends Fragment{
    /**
     抽取基类的好处
     1.可以减少代码量,减少冗余的代码,基类可以放置公共的方法以及公共的属性
     2.不用关心生命周期方法,只需要关心自己定义的方法即可
     3.可以控制哪些方法是选择性实现,哪些方法是具体实现
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        init();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initData();
        initListener();
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * @des 初始化，选择性
     */
    public void init(){

    }

    /**
     * @des 初始化view,必须实现
     * @des 必须实现,但是不知道具体实现,定义成为抽象方法,交给子类具体实现
     */
    public abstract void initView();

    /**
     * @dec 初始化数据，选择性
     */
    public void initData() {

    }

    /**
     * @dec 初始化监听，选择性
     */
    public void initListener() {

    }
}
