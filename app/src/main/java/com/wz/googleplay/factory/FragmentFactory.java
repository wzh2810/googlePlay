package com.wz.googleplay.factory;

import android.support.v4.app.Fragment;

import com.wz.googleplay.base.BaseFragment;
import com.wz.googleplay.fragment.AppFragment;
import com.wz.googleplay.fragment.CategoryFragment;
import com.wz.googleplay.fragment.GameFragment;
import com.wz.googleplay.fragment.HomeFragment;
import com.wz.googleplay.fragment.HotFragment;
import com.wz.googleplay.fragment.RecommendFragment;
import com.wz.googleplay.fragment.SubjectFragment;
import com.wz.googleplay.utils.LogUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wz on 2016/4/16.
 * @描述	      生成/实例化/返回 一个一个的Fragment
 */
public class FragmentFactory {
    public static final int FRAGMENT_HOME      = 0;
    public static final int FRAGMENT_APP       = 1;
    public static final int FRAGMENT_GAME      = 2;
    public static final int FRAGMENT_SUBJECT   = 3;
    public static final int FRAGMENT_RECOMMEND = 4;
    public static final int FRAGMENT_CATEGORY  = 5;
    public static final int FRAGMENT_HOT       = 6;

    private static Map<Integer,BaseFragment> mCacheFragmentMap = new HashMap<>();

    public static BaseFragment createFragment(int position) {

       for (Map.Entry<Integer,BaseFragment> info : mCacheFragmentMap.entrySet()) {
           Integer key = info.getKey();
           BaseFragment value = info.getValue();
           LogUtils.i(key + "-->" + value.toString());
       }

       BaseFragment fragment = null;
       if(mCacheFragmentMap.containsKey(position)) {
           fragment = mCacheFragmentMap.get(position);
           return fragment;
       }
       switch (position) {
           case FRAGMENT_HOME://首页
               fragment = new HomeFragment();
               break;
           case FRAGMENT_APP://应用
               fragment = new AppFragment();
               break;
           case FRAGMENT_GAME://游戏
               fragment = new GameFragment();
               break;
           case FRAGMENT_SUBJECT://专题
               fragment = new SubjectFragment();
               break;

           case FRAGMENT_RECOMMEND://推荐
               fragment = new RecommendFragment();
               break;
           case FRAGMENT_CATEGORY://分类
               fragment = new CategoryFragment();
               break;
           case FRAGMENT_HOT://排行
               fragment = new HotFragment();
               break;

            default:
                break;
       }
        // 保存引用到集合中
        mCacheFragmentMap.put(position,fragment);
       return fragment;
   }
}
