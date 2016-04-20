package com.wz.googleplay.Bean;

/**
 * Created by wz on 2016/4/20.
 */
public class CategoryInfoBean {
    public String	name1;		// 休闲
    public String	name2;		// 棋牌
    public String	name3;		// 益智
    public String	url1;		// image/category_game_0.jpg
    public String	url2;		// image/category_game_1.jpg
    public String	url3;		// image/category_game_2.jpg
    public String	title;		// 游戏

    // 添加一个字段
    public boolean	isTitle;	// 是否是title

    @Override
    public String toString() {
        return "CategoryInfoBean{" +
                "name1='" + name1 + '\'' +
                ", name2='" + name2 + '\'' +
                ", name3='" + name3 + '\'' +
                ", url1='" + url1 + '\'' +
                ", url2='" + url2 + '\'' +
                ", url3='" + url3 + '\'' +
                ", title='" + title + '\'' +
                ", isTitle=" + isTitle +
                '}';
    }
};
