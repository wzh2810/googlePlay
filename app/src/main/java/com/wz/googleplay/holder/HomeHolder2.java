package com.wz.googleplay.holder;



import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.wz.googleplay.Bean.ItemInfoBean;
import com.wz.googleplay.R;
import com.wz.googleplay.base.BaseHolder;
import com.wz.googleplay.config.Constants;
import com.wz.googleplay.utils.StringUtils;
import com.wz.googleplay.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.sephiroth.android.library.picasso.LruCache;
import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by wz on 2016/4/17.
 *
 * @描述 1.提供视图
 * @描述 2.接收数据, 然后进行数据和视图的绑定
 */
public class HomeHolder2 extends BaseHolder<ItemInfoBean> {


    @Bind(R.id.item_appinfo_iv_icon)
    ImageView mItemAppinfoIvIcon;

    @Bind(R.id.item_appinfo_tv_title)
    TextView mItemAppinfoTvTitle;

    @Bind(R.id.item_appinfo_rb_stars)
    RatingBar mItemAppinfoRbStars;

    @Bind(R.id.item_appinfo_tv_size)
    TextView mItemAppinfoTvSize;

    @Bind(R.id.item_appinfo_tv_des)
    TextView mItemAppinfoTvDes;

    /**
     * 初始化持有的视图
     *
     * @return
     */
    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_home_info, null);
        //初始化孩子对象
        ButterKnife.bind(this,view);

        return view;
    }

    @Override
    public void refreshHolderView(ItemInfoBean data) {
        mItemAppinfoTvDes.setText(data.des);
        mItemAppinfoTvSize.setText(StringUtils.formatFileSize(data.size));
        mItemAppinfoTvTitle.setText(data.name);

        mItemAppinfoRbStars.setRating(data.stars);

        //图片加载
        String picUrl = Constants.URLS.IMGBASEURL + data.iconUrl;
        Picasso.with(UIUtils.getContext())//
                .load(picUrl)//
                .fade(300)//淡入淡出的效果
                .withDiskCache(new LruCache(10 * 1024 * 1024))//可能有问题,因为Lrucache只是做内存缓存的.
                // 可能Picasso只是暴露了一个方法没有具体帮我们实现磁盘缓存
                .withCache(new LruCache(4 * 1024 * 1024))
                .into(mItemAppinfoIvIcon);
    }


}
