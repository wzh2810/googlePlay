package com.wz.googleplay.fragment;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;


import com.wz.googleplay.bean.ItemInfoBean;
import com.wz.googleplay.adapter.ItemAdapter;
import com.wz.googleplay.base.BaseFragment;
import com.wz.googleplay.base.LoadingPager;
import com.wz.googleplay.factory.ListViewFactory;
import com.wz.googleplay.holder.ItemHolder;
import com.wz.googleplay.manager.DownLoadInfo;
import com.wz.googleplay.manager.DownLoadManager;
import com.wz.googleplay.protocol.AppProtocol;
import com.wz.googleplay.protocol.HomeProtocol;

import java.util.List;


/**
 * Created by wz on 2016/4/16.
 */
public class AppFragment extends BaseFragment {
    private AppProtocol        mProtocol;
    private List<ItemInfoBean> mDatas;
    private AppAdapter mAdapter;

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

            //根据返回的数据，进行相应的返回操作
            return checkLoadedResult(mDatas);

        } catch (Exception e) {
            e.printStackTrace();
            return LoadingPager.LoadedResult.ERROR;
        }
    }

    @Override
    protected View initSuccessView() {
        ListView listView = ListViewFactory.createListView();

        mAdapter = new AppAdapter(listView, mDatas);
        listView.setAdapter(mAdapter);
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

    @Override
    public void onPause() {
        // 移除所有的观察者
        // 得到观察者
        if(mAdapter != null) {
            List<ItemHolder> itemHolders = mAdapter.mItemHolders;
            for(ItemHolder itemHolder : itemHolders) {
                DownLoadManager.getInstance().deleteObserver(itemHolder);
            }
        }
        super.onPause();
    }

    @Override
    public void onResume() {

        //添加所有的观察者
        if(mAdapter != null) {
            List<ItemHolder> itemHolders = mAdapter.mItemHolders;
            for(ItemHolder itemHolder : itemHolders) {
                DownLoadManager.getInstance().addObserver(itemHolder);
                //收到发布最新状态
                DownLoadInfo downLoadInfo = DownLoadManager.getInstance().getDownLoadInfo(itemHolder.mData);
                DownLoadManager.getInstance().notifyObservers(downLoadInfo);
                // 调用adapter的notifyDataSetChanged
                // mAdapter.notifyDataSetChanged();
            }
        }
        super.onResume();
    }
}
