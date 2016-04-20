package com.wz.googleplay.protocol;

import com.google.gson.JsonArray;
import com.wz.googleplay.Bean.CategoryInfoBean;
import com.wz.googleplay.base.BaseProtocol;
import com.wz.googleplay.utils.LogUtils;
import com.wz.googleplay.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wz on 2016/4/20.
 */
public class CategoryProtocol extends BaseProtocol<List<CategoryInfoBean>> {
    @Override
    public String getInterfaceKey() {
        return "category";
    }

    @Override
    public List<CategoryInfoBean> parseJsonString(String resultJsonString) {
		/*--------------- 使用android sdk里面的结点解析 ---------------*/
        List<CategoryInfoBean> categoryInfoBeans = new ArrayList<>();
        try {
            JSONArray rootJsonArray = new JSONArray(resultJsonString);
            for (int i = 0; i < rootJsonArray.length(); i++) {
                JSONObject itemJsonObject = rootJsonArray.getJSONObject(i);
                // 取出title
                String title = itemJsonObject.getString("title");
                CategoryInfoBean titleBean = new CategoryInfoBean();
                titleBean.title = title;
                titleBean.isTitle = true;

                // 加入集合
                categoryInfoBeans.add(titleBean);

                JSONArray infosJsonArr = itemJsonObject.getJSONArray("infos");
                for (int j = 0; j < infosJsonArr.length(); j++) {
                    JSONObject infoJsonObject = infosJsonArr.getJSONObject(j);
                    String name1 = infoJsonObject.getString("name1");
                    String name2 = infoJsonObject.getString("name2");
                    String name3 = infoJsonObject.getString("name3");
                    String url1 = infoJsonObject.getString("url1");
                    String url2 = infoJsonObject.getString("url2");
                    String url3 = infoJsonObject.getString("url3");
                    CategoryInfoBean infoBean = new CategoryInfoBean();
                    infoBean.name1 = name1;
                    infoBean.name2 = name2;
                    infoBean.name3 = name3;
                    infoBean.url1 = url1;
                    infoBean.url2 = url2;
                    infoBean.url3 = url3;
                    // 加入集合
                    categoryInfoBeans.add(infoBean);
                }
            }
            return categoryInfoBeans;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
