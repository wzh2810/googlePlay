package com.wz.googleplay.Bean;

import java.util.List;

/*
 *@创建者     wz
 *@创建时间     
 *@描述      ${TODO}$
 *
 */
public class ItemInfoBean {
	public long		id;				// 应用的id
	public String	des;			// 应用的描述
	public String	packageName;	// 应用的包名
	public float	stars;			// 应用的评分
	public String	name;			// 应用的名字
	public String	iconUrl;		// 应用的图片地址
	public String	downloadUrl;	// 应用的下载地址
	public long		size;

	/*--------------- 额外添加应用详情里面的字段 ---------------*/
	public String				author;		// 黑马程序员
	public String				date;			// 2015-06-10
	public String				downloadNum;	// 40万+
	public String				version;		// 1.1.0605.0

	public List<ItemSafeBean> safe;			// Array
	public List<String>			screen;		// Array

	public class ItemSafeBean {
		public String	safeDes;		// 已通过安智市场安全检测，请放心使用
		public int	safeDesColor;	// 0
		public String	safeDesUrl;	// app/com.itheima.www/safeDesUrl0.jpg
		public String	safeUrl;		// app/com.itheima.www/safeIcon0.jpg
	}

	@Override
	public String toString() {
		return "ItemInfoBean{" +
				"id=" + id +
				", des='" + des + '\'' +
				", packageName='" + packageName + '\'' +
				", stars=" + stars +
				", name='" + name + '\'' +
				", iconUrl='" + iconUrl + '\'' +
				", downloadUrl='" + downloadUrl + '\'' +
				", size=" + size +
				", author='" + author + '\'' +
				", date='" + date + '\'' +
				", downloadNum='" + downloadNum + '\'' +
				", version='" + version + '\'' +
				", safe=" + safe +
				", screen=" + screen +
				'}';
	}
}
