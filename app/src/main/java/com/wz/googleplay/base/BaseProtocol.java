package com.wz.googleplay.base;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.wz.googleplay.config.Constants;
import com.wz.googleplay.utils.FileUtils;
import com.wz.googleplay.utils.HttpUtil;
import com.wz.googleplay.utils.IOUtils;
import com.wz.googleplay.utils.LogUtils;
import com.wz.googleplay.utils.UIUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wz on 2016/4/18.
 * @描述	      协议封装的基类
 */

public abstract class BaseProtocol<T> {

    /**
     * 加载数据,先内存,然后磁盘,最后网络
     *
     * @param index
     * @return
     * @throws Exception
     */
    public T loadData(int index) throws Exception {
        String key = generateKey(index);
        //1. 先存内存-->返回
        MyApplication app = (MyApplication) UIUtils.getContext();
        Map<String,String> protocolMap = app.getProtocolMap();
        String memJsonString = protocolMap.get(key);

        if(!TextUtils.isEmpty(memJsonString)) {
            LogUtils.i("###从内存加载数据-->" + key);
            // 解析返回即可
            return parseJsonString(memJsonString);
        }
        //2.在存磁盘-->存内存，返回
        T t = loadDataFromLocal(key);
        if(t != null) {
            LogUtils.i("###磁盘加载数据-->" + getCacheFile(key).getAbsolutePath());
            return t;
        }

        // 3.最后网络-->存内存,存磁盘,返回
        t = loadDataFromNet(index);
        return  t;
    }

    /**
     * 从本地加载数据
     * @param key
     * @return
     */
    private T loadDataFromLocal(String key) {
        /* if(文件存在){
		     //读取插入时间
		     //判断是否过期
		     if(未过期){
		         //读取缓存内容
		         //Json解析解析内容
		         if(不为null){
		             //返回并结束
		         }
		     }
		 }*/

        File cacheFile = getCacheFile(key);

        if(cacheFile.exists()) {
            BufferedReader reader = null;
            // 读取缓存的生成时间/插入时间
            try{
                reader = new BufferedReader(new FileReader(cacheFile)) ;
                //读取插入时间
                // 如何组织具体内容和插入时间
                String insertTime = reader.readLine();
                Long inserTime_ = Long.parseLong(insertTime);

                // 当前时间-插入时间和过期时间做判断
                if(System.currentTimeMillis() - inserTime_ < Constants.PROTOCOLTIMEOUT) {
                    //读取缓存内容
                    String diskJsonString = reader.readLine();
                    if(!TextUtils.isEmpty(diskJsonString)) {
                        //存内存
                        MyApplication app = (MyApplication) UIUtils.getContext();
                        app.getProtocolMap().put(key,diskJsonString);
                        //返回
                        return  parseJsonString(diskJsonString);
                    }
                }

            }catch (Exception e) {
                e.printStackTrace();
            } finally {
                IOUtils.close(reader);
            }
        }
        return null;
    }


    /**
     * 得到缓存的文件
     * @param key
     * @return
     */
    private File getCacheFile(String key) {
        String dir = FileUtils.getDir("json");// sdcard/Android/data/包目录/json目
        String name = key;
        return new File(dir,name);
    }

    /**
     * 缓存的唯一索引
     * @param index
     * @return
     */
    private String generateKey(int index) {

        Map<String,Object> requestParamsMap = getRequestParmasMap(index);
        for(Map.Entry<String,Object> info : requestParamsMap.entrySet()) {
            String key = info.getKey();   //"index" "packgeName"
            Object value = info.getValue(); // 0 20 40 具体包名
            return  getInterfaceKey() + "." + value;
        }
      //  return getInterfaceKey() + "." + index;
        return "";
    }

    /**从网络加载数据*/
    private T loadDataFromNet(int index) throws Exception {
         /*=============== 1.得到网络请求回来的jsonString ===============*/
        // 1.创建OkHttpClient实例对象
        OkHttpClient okHttpClient = new OkHttpClient();

        // home
        // ?index=0==>参数
        Map<String, Object> parmasMap =getRequestParmasMap(index);
     //   parmasMap.put("index", index + "");

        // 添加参数
        String urlParamsByMap = HttpUtil.getUrlParamsByMap(parmasMap);

        String url = Constants.URLS.BASEURL + getInterfaceKey() + "?" + urlParamsByMap;

        LogUtils.s("url:" + url);

        // 2.创建一个请求
        Request request = new Request.Builder().get()// get方法
                .url(url)// 对应的url
                .build();

        // 3.发起请求
        Response response = okHttpClient.newCall(request).execute();

        // 4.取出结果
        String resultJsonString = response.body().string();
        /*--------------- 存内存,存本地 ---------------*/
        LogUtils.i("###存内存,存本地");
        MyApplication app = (MyApplication) UIUtils.getContext();
        String key = generateKey(index);
        app.getProtocolMap().put(key,resultJsonString);

        //存本地
        File cacheFile = getCacheFile(key);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(cacheFile));
            // 缓存的生成时间
            writer.write(System.currentTimeMillis() + "");
            // 换行
            writer.newLine();
            // 缓存的具体内容
            writer.write(resultJsonString);

        } catch (Exception e) {

        } finally {
            IOUtils.close(writer);
        }
    //    LogUtils.i("resultJsonString:" + resultJsonString);

        LogUtils.i("resultJsonString:" + resultJsonString);
        /*=============== 2.接续网络请求回来的数据 ===============*/
    //    T t = parseJsonString(resultJsonString);
        return parseJsonString(resultJsonString);
    }

    /**
     * @des 封装请求的参数
     * @des 子类可以覆写该方法，返回更多的参数
     * @param index
     * @return
     */
    public Map<String,Object> getRequestParmasMap(int index) {
        Map<String,Object> defalutParams = new HashMap<>();
        defalutParams.put("index",index + "");
        return defalutParams;
    }

    /**
     * @return
     * @des 返回协议的关键字
     * @des 必须的
     */
    public abstract String getInterfaceKey();

    /**
     * @param resultJsonString
     * @return
     * @des 具体解析请求回来数据
     * @des 必须的
     */
    public abstract T parseJsonString(String resultJsonString);


    public T parseJsonString1(String resultJsonString) {

        // 统一json解析处理
        Gson gson = new Gson();

        Class c = this.getClass();// 得到子类的类型
        Type superType = c.getGenericSuperclass();// 得到子类传递的完整参数化类型
        ParameterizedType pType = (ParameterizedType) superType;// 还需要强转成参数化类型
        Type[] types = pType.getActualTypeArguments();// 得到真实的类型参数们

        Type type = types[0];
        T t = gson.fromJson(resultJsonString, type);
        return t;
    }
}
