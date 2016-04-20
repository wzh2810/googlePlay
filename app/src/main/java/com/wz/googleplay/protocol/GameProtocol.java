package com.wz.googleplay.protocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wz.googleplay.Bean.HomeBean;
import com.wz.googleplay.Bean.ItemInfoBean;
import com.wz.googleplay.base.BaseProtocol;

import java.util.List;

/**
 * Created by wz on 2016/4/18.
 * 对HomeFragment里面网络请求进行封装
 */
public class GameProtocol extends BaseProtocol<List<ItemInfoBean>>{


    @Override
    public String getInterfaceKey() {
        return "game";
    }

    @Override
    public List<ItemInfoBean> parseJsonString(String resultJsonString) {
        Gson gson = new Gson();

        return gson.fromJson(resultJsonString,new TypeToken<List<ItemInfoBean>>(){}.getType());
    }
}
