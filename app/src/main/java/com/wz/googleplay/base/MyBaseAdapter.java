package com.wz.googleplay.base;


import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by wz on 2016/4/17.
 */
public abstract class MyBaseAdapter<ITEMBEANITYPE>  extends BaseAdapter{
    public List<ITEMBEANITYPE> mDataSource;

    public MyBaseAdapter(List DataSource) {
        mDataSource = DataSource;
    }

    @Override
    public int getCount() {
        if(mDataSource != null) {
            return mDataSource.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(mDataSource != null) {
            return mDataSource.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
