package com.wz.googleplay.holder;

import android.view.View;

import com.wz.googleplay.R;
import com.wz.googleplay.base.BaseHolder;
import com.wz.googleplay.utils.UIUtils;

/**
 * Created by wz on 2016/4/23.
 */
public class LeftMenuHolder extends BaseHolder<Object> {
    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.menu_view,null);
        return view;
    }

    @Override
    public void refreshHolderView(Object data) {

    }
}
