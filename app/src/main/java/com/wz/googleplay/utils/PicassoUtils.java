package com.wz.googleplay.utils;

import android.widget.ImageView;

import it.sephiroth.android.library.picasso.LruCache;
import it.sephiroth.android.library.picasso.Picasso;


public class PicassoUtils {
	public static void display(String url, ImageView iv) {
		Picasso.with(UIUtils.getContext()).load(url).fade(300).withCache(new LruCache(4 * 1024 * 1024)).into(iv);
	}
}
