package com.wz.googleplay.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wz.googleplay.bean.SubjectInfoBean;
import com.wz.googleplay.R;
import com.wz.googleplay.base.BaseHolder;
import com.wz.googleplay.config.Constants;
import com.wz.googleplay.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.sephiroth.android.library.picasso.LruCache;
import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by wz on 2016/4/19.
 */
public class SubjectHolder extends BaseHolder<SubjectInfoBean> {

    @Bind(R.id.item_subject_iv_icon)
    ImageView mItemSubjectIvIcon;

    @Bind(R.id.item_subject_tv_title)
    TextView mItemSubjectTvTitle;

    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_subject, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void refreshHolderView(SubjectInfoBean data) {
        mItemSubjectTvTitle.setText(data.des);
        Picasso.with(UIUtils.getContext()).load(Constants.URLS.IMGBASEURL + data.url)
                .withCache(new LruCache(4 * 1024 * 1024)).into(mItemSubjectIvIcon);
    }
}
