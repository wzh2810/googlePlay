package com.wz.googleplay.protocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wz.googleplay.bean.ItemInfoBean;
import com.wz.googleplay.base.BaseProtocol;

import java.util.List;

/**
 * Created by wz on 2016/4/18.
 */
public class AppProtocol extends BaseProtocol<List<ItemInfoBean>> {
    @Override
    public String getInterfaceKey() {
        return "app";
    }

    @Override
    public List<ItemInfoBean> parseJsonString(String resultJsonString) {
        Gson gson = new Gson();
        /*--------------- gson泛型解析 ---------------*/
        return gson.fromJson(resultJsonString,new TypeToken<List<ItemInfoBean>>(){}.getType());
    }
}
