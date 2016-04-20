package com.wz.googleplay.holder;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wz.googleplay.Bean.CategoryInfoBean;
import com.wz.googleplay.R;
import com.wz.googleplay.base.BaseHolder;
import com.wz.googleplay.config.Constants;
import com.wz.googleplay.utils.PicassoUtils;
import com.wz.googleplay.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wz on 2016/4/20.
 */
public class CategoryNormalHolder extends BaseHolder<CategoryInfoBean> {

    @Bind(R.id.item_category_icon_1)
    ImageView mItemCategoryIcon1;

    @Bind(R.id.item_category_name_1)
    TextView mItemCategoryName1;

    @Bind(R.id.item_category_item_1)
    LinearLayout mItemCategoryItem1;

    @Bind(R.id.item_category_icon_2)
    ImageView mItemCategoryIcon2;

    @Bind(R.id.item_category_name_2)
    TextView mItemCategoryName2;

    @Bind(R.id.item_category_item_2)
    LinearLayout mItemCategoryItem2;

    @Bind(R.id.item_category_icon_3)
    ImageView mItemCategoryIcon3;

    @Bind(R.id.item_category_name_3)
    TextView mItemCategoryName3;

    @Bind(R.id.item_category_item_3)
    LinearLayout itemCategoryItem3;

    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_category_normal, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void refreshHolderView(CategoryInfoBean data) {
        setData(data.name1, mItemCategoryName1, data.url1, mItemCategoryIcon1);
        setData(data.name2, mItemCategoryName2, data.url2, mItemCategoryIcon2);
        setData(data.name3, mItemCategoryName3, data.url3, mItemCategoryIcon3);

    }

    public void setData(final String name, TextView textView, String url, ImageView iv) {
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(url)) {

            textView.setText(name);
            PicassoUtils.display(Constants.URLS.IMGBASEURL + url, iv);
            ViewParent parent = textView.getParent();
            ((ViewGroup) parent).setVisibility(View.VISIBLE);

            ((ViewGroup) parent).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UIUtils.getContext(), name, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            ViewParent parent = textView.getParent();
            ((ViewGroup) parent).setVisibility(View.INVISIBLE);
        }
    }

}
