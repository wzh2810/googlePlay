package com.wz.googleplay.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wz.googleplay.bean.ItemInfoBean;
import com.wz.googleplay.R;
import com.wz.googleplay.base.BaseHolder;
import com.wz.googleplay.config.Constants;
import com.wz.googleplay.utils.PicassoUtils;
import com.wz.googleplay.utils.UIUtils;
import com.wz.googleplay.views.RatioLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wz on 2016/4/21.
 */
public class DetailPicHolder extends BaseHolder<ItemInfoBean> {

    @Bind(R.id.app_detail_pic_iv_container)
    LinearLayout mAppDetailPicIvContainer;

    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_detail_pic, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void refreshHolderView(ItemInfoBean data) {
        List<String> screens = data.screen;
        for(int i = 0; i < screens.size(); i++) {
            String screen =screens.get(i);


            RatioLayout rl = new RatioLayout(UIUtils.getContext());
            // 已知宽度,动态计算高度
            // 已知图片的宽高比

            rl.setmPicRatio(150 * 1.0f / 250);
            rl.setRelative(RatioLayout.RELATIVE_WIDTH);

            ImageView iv = new ImageView(UIUtils.getContext());

            //加载图片
            PicassoUtils.display(Constants.URLS.IMGBASEURL + screen,iv);

            // 把ImageView加入到RatioLayout
            rl.addView(iv);

            int screenWidth = UIUtils.getResources().getDisplayMetrics().widthPixels;

            screenWidth = screenWidth - 3 * UIUtils.dp2Px(3);

            int width = screenWidth / 3;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
            if(i != 0) {
                params.leftMargin = UIUtils.dp2Px(3);
            }
            mAppDetailPicIvContainer.addView(rl,params);
        }
    }
}
