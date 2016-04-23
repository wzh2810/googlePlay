package com.wz.googleplay.protocol;

import com.google.gson.Gson;
import com.wz.googleplay.bean.ItemInfoBean;
import com.wz.googleplay.base.BaseProtocol;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wz on 2016/4/21.
 */
public class DetailProtocol extends BaseProtocol<ItemInfoBean> {
    private Object mPackageName;
    public DetailProtocol(Object PackageName) {
        mPackageName = PackageName;
    }
    @Override
    public String getInterfaceKey() {
        return "detail";
    }

    @Override
    public ItemInfoBean parseJsonString(String resultJsonString) {
        Gson gson = new Gson();

        return gson.fromJson(resultJsonString,ItemInfoBean.class);
    }

    @Override
    public Map<String, Object> getRequestParmasMap(int index) {
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("packageName",mPackageName);
        return paramsMap;
    }
}
