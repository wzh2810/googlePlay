package com.wz.googleplay.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.wz.googleplay.bean.ItemInfoBean;
import com.wz.googleplay.activity.DetailActivity;
import com.wz.googleplay.base.BaseHolder;
import com.wz.googleplay.base.SuperBaseAdapter;
import com.wz.googleplay.holder.ItemHolder;
import com.wz.googleplay.manager.DownLoadManager;
import com.wz.googleplay.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wz on 2016/4/20.
 * @描述	      HomeFragment,AppFragment,GameFragment 其中的Adapter进行了简单的抽取
 * @描述	      点击事件可以统一写到基类里面来
 */
public class ItemAdapter extends SuperBaseAdapter<ItemInfoBean> {

    // 使用集合保存ItemAdapter中对应的所有的ItemHolder
    public List<ItemHolder>	mItemHolders	= new ArrayList<>();

    public ItemAdapter(AbsListView absListView, List DataSource) {
        super(absListView, DataSource);
    }

    @Override
    public BaseHolder getSpecialHolder(int position) {
        ItemHolder itemHolder = new ItemHolder();
        mItemHolders.add(itemHolder);
        // 加入观察者到观察者集合中
        DownLoadManager.getInstance().addObserver(itemHolder);
        return itemHolder;
    }

    @Override
    public void onNormalItemClick(AdapterView<?> parent, View view, int position, long id) {
        ItemInfoBean itemInfoBean = (ItemInfoBean) mDataSource.get(position);
        Intent intent = new Intent(UIUtils.getContext(), DetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(DetailActivity.PACKAGENAME,itemInfoBean.packageName);

        UIUtils.getContext().startActivity(intent);
        super.onNormalItemClick(parent, view, position, id);
    }
}
