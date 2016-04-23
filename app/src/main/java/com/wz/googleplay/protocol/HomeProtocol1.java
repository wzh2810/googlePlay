package com.wz.googleplay.protocol;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.wz.googleplay.bean.HomeBean;
import com.wz.googleplay.config.Constants;
import com.wz.googleplay.utils.HttpUtil;
import com.wz.googleplay.utils.LogUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wz on 2016/4/18.
 * 对HomeFragment里面网络请求进行封装
 */
public class HomeProtocol1 {

    /**
     * 加载数据
     * @param index
     * @return
     * @throws Exception
     */
    public HomeBean loadData(int index) throws Exception{
        // 1.创建OkHttpClient实例对象
        OkHttpClient okHttpClient = new OkHttpClient();

        // home
        // ?index=0==>参数
        Map<String, Object> parmasMap = new HashMap<>();
        parmasMap.put("index", index + "");

        // 添加参数
        String urlParamsByMap = HttpUtil.getUrlParamsByMap(parmasMap);

        String url = Constants.URLS.BASEURL + "home?" + urlParamsByMap;

        LogUtils.s("url:" + url);

        // 2.创建一个请求
        Request request = new Request.Builder().get()// get方法
                .url(url)// 对应的url
                .build();

        // 3.发起请求
        Response response = okHttpClient.newCall(request).execute();

        // 4.取出结果
        String resultJsonString = response.body().string();

        LogUtils.i("resultJsonString:" + resultJsonString);

        // 解析json
        Gson gson = new Gson();
        HomeBean homeBean = gson.fromJson(resultJsonString, HomeBean.class);
        return homeBean;
    }

}
