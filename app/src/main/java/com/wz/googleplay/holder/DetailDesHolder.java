package com.wz.googleplay.holder;

import android.view.View;
import android.widget.TextView;

import com.wz.googleplay.Bean.ItemInfoBean;
import com.wz.googleplay.base.BaseHolder;
import com.wz.googleplay.utils.UIUtils;

/**
 * Created by wz on 2016/4/21.
 */
public class DetailDesHolder extends BaseHolder<ItemInfoBean> {
    private TextView mTv;
    @Override
    public View initHolderView() {
       mTv = new TextView(UIUtils.getContext());
        return mTv;
    }

    @Override
    public void refreshHolderView(ItemInfoBean data) {
        mTv.setText(this.getClass().getSimpleName());
    }
}
