package com.wz.googleplay.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.wz.googleplay.bean.CategoryInfoBean;
import com.wz.googleplay.base.BaseHolder;
import com.wz.googleplay.utils.UIUtils;

/**
 * Created by wz on 2016/4/20.
 */
public class CategoryTitleHolder extends BaseHolder<CategoryInfoBean> {
    private TextView mTvTitle;
    @Override
    public View initHolderView() {
        mTvTitle = new TextView(UIUtils.getContext());
        int padding = UIUtils.dp2Px(5);
        mTvTitle.setPadding(padding,padding,padding,padding);
        mTvTitle.setTextColor(Color.GREEN);
        return mTvTitle;
    }

    @Override
    public void refreshHolderView(CategoryInfoBean data) {
    mTvTitle.setText(data.title);
    }
}
