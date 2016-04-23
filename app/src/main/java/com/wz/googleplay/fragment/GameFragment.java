package com.wz.googleplay.fragment;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.wz.googleplay.bean.ItemInfoBean;
import com.wz.googleplay.base.BaseFragment;
import com.wz.googleplay.base.BaseHolder;
import com.wz.googleplay.base.LoadingPager;
import com.wz.googleplay.base.SuperBaseAdapter;
import com.wz.googleplay.factory.ListViewFactory;
import com.wz.googleplay.holder.ItemHolder;
import com.wz.googleplay.protocol.GameProtocol;

import java.util.List;

/**
 * Created by wz on 2016/4/16.
 */
public class GameFragment extends BaseFragment {
    private List<ItemInfoBean> mDatas;
    private GameProtocol mProtocol;
    /**
     * @return
     * @des 真正的在子线程里面开始加载想加载的数据
     * @call 想要加载数据的时候
     * @return 返回值只能是(1,2 ,3) 中的某一个值,如果设置为其他值,会影响视图的显示逻辑
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

        listView.setAdapter(new GameAdapter(listView,mDatas));
        return listView;
    }

    class GameAdapter extends SuperBaseAdapter<ItemInfoBean> {

        public GameAdapter(AbsListView absListView, List dataSource) {
            super(absListView, dataSource);
        }
        /**
         * @return
         * @des 返回BaseHolder子类实例对象
         * @call 走到getView方法中而且(convertView == null)的时候被调用
         * @param position
         */
        @Override
        public BaseHolder getSpecialHolder(int position) {
            return new ItemHolder();
        }

        @Override
        public List<ItemInfoBean> onLoadMore() throws Exception {
            return mProtocol.loadData(mDatas.size());
        }
    }

//    class GameAdapter extends MyBaseAdapter<String> {
//
//        public GameAdapter(List DataSource) {
//            super(DataSource);
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            GameHolder gameHolder = null;
//            if(convertView == null) {
//                gameHolder = new GameHolder();
//            } else {
//                gameHolder = (GameHolder) convertView.getTag();
//            }
//            gameHolder.setDataAndRefreshHolderView(mDatas.get(position));
//            return gameHolder.mRootView;
//        }
//    }

//    class GameAdapter extends MyBaseAdapter {
//        public GameAdapter(List dataSouce) {
//            super(dataSouce);
//        }
//
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder = null;
//            if(convertView == null) {
//                holder = new ViewHolder();
//                convertView = View.inflate(UIUtils.getContext(), R.layout.item_temp,null);
//                holder.tv_1 = (TextView) convertView.findViewById(R.id.tv_1);
//                holder.tv_2 = (TextView) convertView.findViewById(R.id.tv_2);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            String data = mDatas.get(position);
//            holder.tv_1.setText("我是头--" + data);
//            holder.tv_2.setText("我是尾--" +data);
//            return convertView;
//        }
//    }
//
//    class ViewHolder {
//        TextView tv_1;
//        TextView tv_2;
//    }
}
