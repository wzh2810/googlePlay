package com.wz.googleplay.fragment;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.wz.googleplay.Bean.CategoryInfoBean;
import com.wz.googleplay.base.BaseFragment;
import com.wz.googleplay.base.BaseHolder;
import com.wz.googleplay.base.LoadingPager;
import com.wz.googleplay.base.SuperBaseAdapter;
import com.wz.googleplay.factory.ListViewFactory;
import com.wz.googleplay.holder.CategoryNormalHolder;
import com.wz.googleplay.holder.CategoryTitleHolder;
import com.wz.googleplay.protocol.CategoryProtocol;
import com.wz.googleplay.utils.LogUtils;

import java.util.List;

/**
 * Created by wz on 2016/4/16.
 */
public class CategoryFragment extends BaseFragment {

    private List<CategoryInfoBean>	mDatas;

    /**
     * @return
     * @des 真正的在子线程里面开始加载想加载的数据
     * @call 想要加载数据的时候
     * @return 返回值只能是(1,2 ,3) 中的某一个值,如果设置为其他值,会影响视图的显示逻辑
     */
    @Override
    protected LoadingPager.LoadedResult initData() {
        CategoryProtocol protocol = new CategoryProtocol();
        try {
            mDatas = protocol.loadData(0);
            // 打印一下category对应的list数据
            LogUtils.printList(mDatas);
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
        listView.setAdapter(new CategoryAdapter(listView, mDatas));
        return listView;
    }

    class CategoryAdapter extends SuperBaseAdapter<CategoryInfoBean> {

        public CategoryAdapter(AbsListView absListView, List dataSource) {
            super(absListView, dataSource);
        }

        @Override
        public BaseHolder getSpecialHolder(int position) {// holder=data+view
            // data
            CategoryInfoBean infoBean = mDatas.get(position);
            if (infoBean.isTitle) {
                return new CategoryTitleHolder();
            } else {
                return new CategoryNormalHolder();
            }
        }

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount() + 1;// 2+1 = 3
        }

        @Override
        public int getNormalItemViewType(int position) {
            CategoryInfoBean infoBean = mDatas.get(position);
            if (infoBean.isTitle) {
                return 1;
            } else {
                return 2;
            }
            // return super.getNormalItemViewType();
        }
    }
}