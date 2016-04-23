package com.wz.googleplay.config;


import com.wz.googleplay.utils.LogUtils;

public class Constants {
    /**
     * LogUtils.LEVEL_ALL:显示所有的日志信息-->打开日志开关
     * LogUtils.LEVEL_OFF:隐藏所有的日志信息-->关闭日志开关
     */
    public static final int DEBUGLEVEL = LogUtils.LEVEL_ALL;

    public static final long	PROTOCOLTIMEOUT	= 60 * 60 * 1000;
    public static final class URLS {

        public static final String BASEURL = "http://192.168.3.19:8080/GooglePlayServer/";
        public static final String IMGBASEURL = BASEURL+"image?name=";
        public static final String	DOWNLOADBASEURL	= BASEURL + "download?";

    }
}
