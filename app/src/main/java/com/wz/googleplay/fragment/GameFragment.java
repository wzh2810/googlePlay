package com.wz.googleplay.fragment;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.wz.googleplay.adapter.ItemAdapter;
import com.wz.googleplay.bean.ItemInfoBean;
import com.wz.googleplay.base.BaseFragment;
import com.wz.googleplay.base.BaseHolder;
import com.wz.googleplay.base.LoadingPager;
import com.wz.googleplay.base.SuperBaseAdapter;
import com.wz.googleplay.factory.ListViewFactory;
import com.wz.googleplay.holder.ItemHolder;
import com.wz.googleplay.manager.DownLoadInfo;
import com.wz.googleplay.manager.DownLoadManager;
import com.wz.googleplay.protocol.GameProtocol;

import java.util.List;

/**
 * Created by wz on 2016/4/16.
 */
public class GameFragment extends BaseFragment {
    private List<ItemInfoBean> mDatas;
    private GameProtocol mProtocol;
    private GameAdapter mAdapter;

    /**
     * @return 返回值只能是(1, 2, 3) 中的某一个值,如果设置为其他值,会影响视图的显示逻辑
     * @des 真正的在子线程里面开始加载想加载的数据
     * @call 想要加载数据的时候
     */
    @Override
    protected LoadingPager.LoadedResult initData() {

        mProtocol = new GameProtocol();
        try {
            mDatas = mProtocol.loadData(0);
            return checkLoadedResult(mDatas);
        } catch (Exception e) {
            e.printStackTrace();
            return LoadingPager.LoadedResult.ERROR;
        }


    }

    /**
     * @return
     * @des 展示具体的成功视图
     * @call 数据加载完成, 并且是数据加载成功的时候
     */
    @Override
    protected View initSuccessView() {
        ListView listView = ListViewFactory.createListView();

        mAdapter = new GameAdapter(listView, mDatas);
        listView.setAdapter(mAdapter);
        return listView;
    }

    class GameAdapter extends ItemAdapter {

        public GameAdapter(AbsListView absListView, List dataSource) {
            super(absListView, dataSource);
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

        if (mAdapter != null) {
            List<ItemHolder> itemHolders = mAdapter.mItemHolders;
            for (ItemHolder itemHolder : itemHolders) {
                DownLoadManager.getInstance().deleteObserver(itemHolder);
            }
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        // 添加所有的观察者
        if (mAdapter != null) {
            List<ItemHolder> itemHolders = mAdapter.mItemHolders;
            for (ItemHolder itemHolder : itemHolders) {
                DownLoadManager.getInstance().addObserver(itemHolder);
                // 收到发布最新状态
//                DownLoadInfo downLoadInfo = DownLoadManager.getInstance().getDownLoadInfo(itemHolder.mData);
//                DownLoadManager.getInstance().notifyObservers(downLoadInfo);
                // 调用adapter的notifyDataSetChanged
                 mAdapter.notifyDataSetChanged();
            }
        }
        super.onResume();
    }

}
