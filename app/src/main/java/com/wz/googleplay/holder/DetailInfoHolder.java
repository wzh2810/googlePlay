package com.wz.googleplay.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.wz.googleplay.bean.ItemInfoBean;
import com.wz.googleplay.R;
import com.wz.googleplay.base.BaseHolder;
import com.wz.googleplay.config.Constants;
import com.wz.googleplay.utils.PicassoUtils;
import com.wz.googleplay.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wz on 2016/4/21.
 */
public class DetailInfoHolder extends BaseHolder<ItemInfoBean> {

    @Bind(R.id.app_detail_info_iv_icon)
    ImageView mAppDetailInfoIvIcon;
    @Bind(R.id.app_detail_info_tv_name)
    TextView mAppDetailInfoTvName;
    @Bind(R.id.app_detail_info_rb_star)
    RatingBar mAppDetailInfoRbStar;
    @Bind(R.id.app_detail_info_tv_downloadnum)
    TextView mAppDetailInfoTvDownloadnum;
    @Bind(R.id.app_detail_info_tv_version)
    TextView mAppDetailInfoTvVersion;
    @Bind(R.id.app_detail_info_tv_time)
    TextView mAppDetailInfoTvTime;
    @Bind(R.id.app_detail_info_tv_size)
    TextView mAppDetailInfoTvSize;

    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_detail_info, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void refreshHolderView(ItemInfoBean data) {
        mAppDetailInfoTvName.setText(data.name);

        String date = UIUtils.getString(R.string.detail_date,data.date);
        String downLoadNum = UIUtils.getString(R.string.detail_downnum,data.downloadNum);
        String size = UIUtils.getString(R.string.detail_size,data.size);
        String version = UIUtils.getString(R.string.detail_version,data.version);

        mAppDetailInfoTvDownloadnum.setText(downLoadNum);
        mAppDetailInfoTvSize.setText(size);
        mAppDetailInfoTvTime.setText(date);
        mAppDetailInfoTvVersion.setText(version);

        PicassoUtils.display(Constants.URLS.IMGBASEURL + data.iconUrl,mAppDetailInfoIvIcon);
        mAppDetailInfoRbStar.setRating(data.stars);
    }
}
