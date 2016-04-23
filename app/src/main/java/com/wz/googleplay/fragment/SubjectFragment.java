package com.wz.googleplay.fragment;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.wz.googleplay.bean.SubjectInfoBean;
import com.wz.googleplay.base.BaseFragment;
import com.wz.googleplay.base.BaseHolder;
import com.wz.googleplay.base.LoadingPager;
import com.wz.googleplay.base.SuperBaseAdapter;
import com.wz.googleplay.factory.ListViewFactory;
import com.wz.googleplay.holder.SubjectHolder;
import com.wz.googleplay.protocol.SubjectProtocol;

import java.util.List;

/**
 * Created by wz on 2016/4/16.
 */
public class SubjectFragment extends BaseFragment {
    private List<SubjectInfoBean> mDatas;
    private SubjectProtocol mProtocol;

    /**
     * @return
     * @des 真正的在子线程里面开始加载想加载的数据
     * @call 想要加载数据的时候
     * @return 返回值只能是(1,2 ,3) 中的某一个值,如果设置为其他值,会影响视图的显示逻辑
     */
    @Override
    protected LoadingPager.LoadedResult initData() {
       mProtocol = new SubjectProtocol();
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

        listView.setAdapter(new SubjectAdapter(listView,mDatas));
        return listView;
    }
    class SubjectAdapter extends SuperBaseAdapter<SubjectInfoBean> {

        public SubjectAdapter(AbsListView absListView, List DataSource) {
            super(absListView, DataSource);
        }

        @Override
        public BaseHolder getSpecialHolder(int position) {
            return new SubjectHolder();
        }
    }
}