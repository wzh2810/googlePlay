package com.wz.googleplay.protocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wz.googleplay.base.BaseProtocol;

import java.util.List;

/**
 * Created by wz on 2016/4/20.
 */
public class HotProtocol extends BaseProtocol<List<String>> {
    @Override
    public String getInterfaceKey() {
        return "hot";
    }

    @Override
    public List<String> parseJsonString(String resultJsonString) {
        Gson gson = new Gson();
        return gson.fromJson(resultJsonString,new TypeToken<List<String>>(){}.getType());
    }
}
