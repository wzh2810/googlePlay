package com.wz.googleplay.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.wz.googleplay.utils.UIUtils;

import java.util.List;
import java.util.Map;

/*
 *@创建者     wz
 *@创建时间
 *@描述      ${TODO}$
 *
 */
public abstract class BaseFragment extends Fragment {
    public LoadingPager mLoadingPager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
       if(mLoadingPager == null) {
           mLoadingPager = new LoadingPager(UIUtils.getContext()) {
               /**
                * @return
                * @des 真正的在子线程里面开始加载想加载的数据
                * @call 想要加载数据的时候
                */
               @Override
               public LoadedResult initData() {
                   return BaseFragment.this.initData();
               }

               /**
                * @des 展示具体的成功视图
                * @call 数据加载完成, 并且是数据加载成功的时候
                * @return
                */
               @Override
               public View initSuccessView() {
                   return BaseFragment.this.initSuccessView();
               }
           };
       } else {
           /**
            如果使用eclipse开发,需要加上如下代码
            */
           ViewParent parent = mLoadingPager.getParent();
           if (parent != null && parent instanceof ViewGroup) {
               ((ViewGroup) parent).removeView(mLoadingPager);
           }
       }

        //触发加载数据
    //    loadingPager.triggerLoadData();
        return mLoadingPager;
    }

      /*############### BaseFragment里面定义了2个抽象方法 ###############*/
    /**
     * @return
     * @des loadingPager里面的initData方法的具体实现, 转到这里来
     * @des 和loadingPager里面的initData方法同名
     * @des 真正的在子线程里面开始加载想加载的数据
     * @des 此时BaseFragment其实也不知道具体如何加载数据(要么选择, 要么必须实现)
     * @call 想要加载数据的时候
     */
    protected abstract LoadingPager.LoadedResult initData();

    /**
     * @return
     * @des loadingPager里面的initData方法的具体实现, 转到这里来
     * @des 和loadingPager里面的initSuccessView方法同名
     * @des 展示具体的成功视图
     * @des 此时BaseFragment其实也不知道具体展示什么样的成功视图
     * @call 数据加载完成, 并且是数据加载成功的时候
     */
    protected abstract View initSuccessView();

    /**
     //页面(Fragment/activity)显示分析
     //Fragment共性-->页面共性-->视图的展示
     /**
     任何应用其实就只有4种页面类型
     ① 加载页面
     ② 错误页面
     ③ 空页面
     ④ 成功页面-->具体的,复杂一些
     ①②③三种页面一个应用基本是固定(静态的)的
     每一个fragment对应的页面④就不一样
     进入应用的时候显示①,②③④需要加载数据之后才知道显示哪个
     */

    // 1.数据的加载-->data
    // 2.视图的展示-->view

    // 数据加载的流程
    /**
     ① 触发加载  	进入页面开始加载/点击某一个按钮的时候加载
     ② 异步加载数据  -->显示加载视图
     ③ 处理加载结果
     ① 成功-->显示成功视图
     ② 失败
     ① 数据为空-->显示空视图
     ② 数据加载失败-->显示加载失败的视图(重试操作)
     */

    /**
     * h根据返回的数据，进行相应的返回操作
     * @param resResult
     * @return
     */
    public LoadingPager.LoadedResult checkLoadedResult(Object resResult) {
        if(resResult == null) {
            return LoadingPager.LoadedResult.EMPTY;
        }

        //list
        if(resResult instanceof List) {
            if(((List) resResult).size() == 0) {
                return LoadingPager.LoadedResult.EMPTY;
            }
        }

        //map
        if(resResult instanceof Map) {
            return LoadingPager.LoadedResult.EMPTY;
        }

        return  LoadingPager.LoadedResult.SUCCESS;
    }
}

