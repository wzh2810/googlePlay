package com.wz.googleplay.protocol;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.wz.googleplay.Bean.HomeBean;
import com.wz.googleplay.base.BaseProtocol;

/**
 * Created by wz on 2016/4/18.
 * 对HomeFragment里面网络请求进行封装
 */
public class HomeProtocol extends BaseProtocol<HomeBean>{


    @Override
    public String getInterfaceKey() {
        return "home";
    }

    @Override
    public HomeBean parseJsonString(String resultJsonString) {
        Gson gson = new Gson();
        HomeBean homeBean = gson.fromJson(resultJsonString,HomeBean.class);
        return homeBean;
    }
}
