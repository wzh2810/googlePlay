package com.wz.googleplay.fragment;

import android.os.SystemClock;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;


import com.wz.googleplay.Bean.ItemInfoBean;
import com.wz.googleplay.base.BaseFragment;
import com.wz.googleplay.base.BaseHolder;
import com.wz.googleplay.base.LoadingPager;
import com.wz.googleplay.base.SuperBaseAdapter;
import com.wz.googleplay.factory.ListViewFactory;
import com.wz.googleplay.holder.ItemHolder;
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

    class AppAdapter extends SuperBaseAdapter<ItemInfoBean> {

        public AppAdapter(AbsListView absListView, List DataSource) {
            super(absListView, DataSource);
        }

        @Override
        public BaseHolder getSpecialHolder(int position) {
            return new ItemHolder();
        }

        @Override
        public List<ItemInfoBean> onLoadMore() throws Exception {
            SystemClock.sleep(2000);
            return mProtocol.loadData(mDatas.size());
        }
    }
}
