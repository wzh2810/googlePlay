package com.wz.googleplay.factory;

import android.graphics.Color;
import android.widget.ListView;

import com.wz.googleplay.utils.UIUtils;

/**
 * Created by wz on 2016/4/18.
 */
public class ListViewFactory {
    public static ListView createListView() {
        ListView listView = new ListView(UIUtils.getContext());
        // 常规的设置
        listView.setCacheColorHint(Color.TRANSPARENT);
        listView.setDividerHeight(0);
        listView.setFadingEdgeLength(0);
//        listView.setFastScrollEnabled(true);

        return listView;
    }
}
