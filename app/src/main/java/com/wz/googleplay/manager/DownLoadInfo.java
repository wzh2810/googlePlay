package com.wz.googleplay.manager;

/**
 * Created by wz on 2016/4/22.
 * @描述	      组合/封装/整合   和  下载相关的参数
 */
public class DownLoadInfo {

    public String	downLoadUrl;								// 下载地址
    public String	savePath;									// apk文件保存的具体路径
    public int		state	= DownLoadManager.STATE_UNDOWNLOAD; // 记录当前的下载状态
    public String	packageName;								// apk对应的包名
    public long		max;										// 进度的最大值
    public long		progress;									// 进度的当前值

}
