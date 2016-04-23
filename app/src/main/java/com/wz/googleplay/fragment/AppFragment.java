package com.wz.googleplay.fragment;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;


import com.wz.googleplay.bean.ItemInfoBean;
import com.wz.googleplay.adapter.ItemAdapter;
import com.wz.googleplay.base.BaseFragment;
import com.wz.googleplay.base.LoadingPager;
import com.wz.googleplay.factory.ListViewFactory;
import com.wz.googleplay.protocol.AppProtocol;

import java.util.List;


/**
 * Created by wz on 2016/4/16.
 */
public class AppFragment extends BaseFragment {
    private List<ItemInfoBean> mDatas;
    private AppProtocol mProtocol;

    /**
     * @return
     * @des 真正的在子线程里面开始加载想加载的数据
     * @call 想要加载数据的时候
     * @return 返回值只能是(1,2 ,3) 中的某一个值,如果设置为其他值,会影响视图的显示逻辑
     */
    @Override
    protected LoadingPager.LoadedResult initData() {

        mProtocol = new AppProtocol();
        try{
            mDatas = mProtocol.loadData(0);
            return checkLoadedResult(mDatas);
        } catch (Exception e) {
            e.printStackTrace();
            return LoadingPager.LoadedResult.ERROR;
        }
    }

    @Override
    protected View initSuccessView() {
        ListView listView = ListViewFactory.createListView();
        listView.setAdapter(new AppAdapter(listView,mDatas));
        return listView;
    }

    class AppAdapter extends ItemAdapter {

        public AppAdapter(AbsListView absListView, List DataSource) {
            super(absListView, DataSource);
        }



        @Override
        public List<ItemInfoBean> onLoadMore() throws Exception {

            return mProtocol.loadData(mDatas.size());
        }
    }
}
