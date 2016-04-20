package com.wz.googleplay.fragment;

import android.os.SystemClock;

import android.view.View;

import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.wz.googleplay.Bean.HomeBean;
import com.wz.googleplay.Bean.ItemInfoBean;
import com.wz.googleplay.base.BaseFragment;
import com.wz.googleplay.base.BaseHolder;
import com.wz.googleplay.base.LoadingPager;
import com.wz.googleplay.base.SuperBaseAdapter;
import com.wz.googleplay.factory.ListViewFactory;
import com.wz.googleplay.holder.HomePictureHolder;
import com.wz.googleplay.holder.ItemHolder;
import com.wz.googleplay.protocol.HomeProtocol;
import com.wz.googleplay.utils.UIUtils;

import java.util.List;

/**
 * Created by wz on 2016/4/16.
 */
public class HomeFragment extends BaseFragment {

    private List<ItemInfoBean> mDatas;
    private List<String> mPicstures;
    HomeProtocol  mProtocol;

    /**
     * @return 返回值只能是(1, 2, 3) 中的某一个值,如果设置为其他值,会影响视图的显示逻辑
     * @des 真正的在子线程里面开始加载想加载的数据
     * @call 想要加载数据的时候
     */
    @Override
    protected LoadingPager.LoadedResult initData() {
        try {
            // 真正的加载数据
            // 同步
//            // 1.创建OkHttpClient实例对象
//            OkHttpClient okHttpClient = new OkHttpClient();
//
//            // home
//            // ?index=0==>参数
//            Map<String, Object> parmasMap = new HashMap<>();
//            parmasMap.put("index", 0);
//
//            // 添加参数
//            String urlParamsByMap = HttpUtil.getUrlParamsByMap(parmasMap);
//
//            String url = Constants.URLS.BASEURL + "home?" + urlParamsByMap;
//
//            LogUtils.s("url:" + url);
//
//            // 2.创建一个请求
//            Request request = new Request.Builder().get()// get方法
//                    .url(url)// 对应的url
//                    .build();
//
//            // 3.发起请求
//            Response response = okHttpClient.newCall(request).execute();
//
//            // 4.取出结果
//            String resultJsonString = response.body().string();
//
//            LogUtils.i("resultJsonString:" + resultJsonString);
//
//            // 解析json
//            Gson gson = new Gson();
//            HomeBean homeBean = gson.fromJson(resultJsonString, HomeBean.class);
            /**----------------协议简单封装以后----------------------**/
            mProtocol = new HomeProtocol();
            HomeBean homeBean = mProtocol.loadData(0);

            // 根据返回的数据,进行相应的返回操作
            LoadingPager.LoadedResult state = checkLoadedResult(homeBean);
            if (state != LoadingPager.LoadedResult.SUCCESS) {// homBean说明出现问题
                return state;
            }

            state = checkLoadedResult(homeBean.list);
            if (state != LoadingPager.LoadedResult.SUCCESS) {// homeBean.list出现问题
                return state;
            }

            // 走到这里来-->说明没有问题

            // 保存数据到成员变量里面去
            mDatas = homeBean.list;
            mPicstures = homeBean.picture;

            return state;// LoadedResult.SUCCESS
        } catch (Exception e) {
            e.printStackTrace();
            return LoadingPager.LoadedResult.ERROR;
        }
    }

    /**
     * @return
     * @des 展示具体的成功视图
     * @call 数据加载完成, 并且是数据加载成功的时候
     */
    @Override
    protected View initSuccessView() {
		/*--------------- 确定展示的view ---------------*/
       ListView listView = ListViewFactory.createListView();

        //添加headerView
        HomePictureHolder homePictureHolder = new HomePictureHolder();
        listView.addHeaderView(homePictureHolder.mRootView);
        homePictureHolder.setDataAndRefreshHolderView(mPicstures);

		/*--------------- 数据和视图的绑定 ---------------*/

        listView.setAdapter(new HomeAdapter(listView,mDatas));
        return listView;
    }

    class HomeAdapter extends SuperBaseAdapter<ItemInfoBean> {
        public HomeAdapter(AbsListView absListView, List dataSource) {
            super(absListView, dataSource);
        }


        /**
         * @return
         * @des 返回BaseHolder子类实例对象
         * @call 走到getView方法中而且(convertView == null)的时候被调用
         * @param position
         */
        @Override
        public BaseHolder getSpecialHolder(int position) {
            return new ItemHolder();
        }

        @Override
        public void onNormalItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(UIUtils.getContext(), mDatas.get(position).packageName, Toast.LENGTH_SHORT).show();
            super.onNormalItemClick(parent, view, position, id);
        }

        /**
         * @return
         * @des 真正的在子线程中加载更多
         */
        @Override
        public List<ItemInfoBean> onLoadMore() throws Exception {
            SystemClock.sleep(2000);
           /* // 真正的加载数据
            // 同步
            // 1.创建OkHttpClient实例对象
            OkHttpClient okHttpClient = new OkHttpClient();

            // home
            // ?index=0==>参数
            Map<String, Object> parmasMap = new HashMap<>();
            parmasMap.put("index", mDatas.size());

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
            HomeBean homeBean = gson.fromJson(resultJsonString, HomeBean.class);*/

            /**----------------协议简单封装以后----------------------**/
            mProtocol = new HomeProtocol();
            HomeBean homeBean = mProtocol.loadData(mDatas.size());

            if (homeBean != null) {
                return homeBean.list;
            }
            return super.onLoadMore();
        }
    }

}




//class HomeFragment1 extends BaseFragment {
//
//    private List<ItemInfoBean>	mDatas;
//    private List<String> mPicstures;
//
//    /**
//     * @return
//     * @des 真正的在子线程里面开始加载想加载的数据
//     * @call 想要加载数据的时候
//     * @return 返回值只能是(1,2 ,3) 中的某一个值,如果设置为其他值,会影响视图的显示逻辑
//     */
//    @Override
//    protected LoadingPager.LoadedResult initData() {
//        try {
//
//            //真正的加载数据
//            //同步
//            //1.创建OKHttpClient实例对象
//            OkHttpClient okHttpClient = new OkHttpClient();
//
//            Map<String, Object> parmasMap = new HashMap<>();
//            parmasMap.put("index", 0);
//
//            // 添加参数
//            String urlParamsByMap = HttpUtil.getUrlParamsByMap(parmasMap);
//
//            String url = Constants.URLS.BASEURL + "home?" + urlParamsByMap;
//
//            // 2.创建一个请求
//            Request request = new Request.Builder().get()// get方法
//                    .url(url)// 对应的url
//                    .build();
//
//            // 3.发起请求
//            Response response = okHttpClient.newCall(request).execute();
//
//            // 4.取出结果
//            String resultJsonString = response.body().string();
//
//            LogUtils.i("resultJsonString:" + resultJsonString);
//            // LogUtils.i("resultJsonString" + resultJsonString);
//            //解析json
//            Gson gson = new Gson();
//            HomeBean homeBean = gson.fromJson(resultJsonString,HomeBean.class);
//
//            //根据返回的数据，进行相应的返回操作
//            LoadingPager.LoadedResult state = checkLoadedResult(homeBean);
//            if(state != LoadingPager.LoadedResult.SUCCESS) {// homBean说明出现问题
//                return state;
//            }
//
//            state = checkLoadedResult(homeBean.list);
//            if(state != LoadingPager.LoadedResult.SUCCESS) { // homBean.list说明出现问题
//                return  state;
//            }
//
//            // 走到这里来-->说明没有问题
//
//            // 保存数据到成员变量里面去
//            mDatas = homeBean.list;
//            mPicstures = homeBean.picture;
//            return state;// LoadedResult.SUCCESS
//        } catch (Exception e) {
//            e.printStackTrace();
//            return LoadingPager.LoadedResult.ERROR;
//        }
//
//
//    }
//
//    @Override
//    protected View initSuccessView() {
//        ListView listView = new ListView(UIUtils.getContext());
//        // 常规的设置
//        listView.setCacheColorHint(Color.TRANSPARENT);
//        listView.setDividerHeight(0);
//        listView.setFadingEdgeLength(0);
//        listView.setFastScrollEnabled(true);
//
//		/*--------------- 数据和视图的绑定 ---------------*/
//
//        listView.setAdapter(new HomeAdapter(mDatas));
//        return listView;
//    }
//
//    class  HomeAdapter extends SuperBaseAdapter<ItemInfoBean>  {
//
//        public HomeAdapter(List DataSource) {  // 初始化dataSource
//            super(DataSource);
//        }
//
//        /**
//         * @return
//         * @des 返回BaseHolder子类实例对象
//         * @call 走到getView方法中而且(convertView == null)的时候被调用
//         */
//        @Override
//        public BaseHolder getSpecialHolder() {
//            return new ItemHolder();
//        }
//
//        /**
//         * 真正在子类线程中加载更多
//         * @return
//         * @throws Exception
//         */
//        @Override
//        public List<ItemInfoBean> onLoadMore() throws Exception {
//            SystemClock.sleep(2000);
//            // 真正的加载数据
//            // 同步
//            // 1.创建OkHttpClient实例对象
//            OkHttpClient okHttpClient = new OkHttpClient();
//
//            // home
//            // ?index=0==>参数
//            Map<String,Object> parmasMap = new
//  HashMap<>();
//            parmasMap.put("index",mDatas.size());
//
//            //添加参数
//            String urlParamsByMap = HttpUtil.getUrlParamsByMap(parmasMap);
//            String url = Constants.URLS.BASEURL + "home?" + urlParamsByMap;
//
//            LogUtils.s("url:" + url);
//
//            //2.创建一个请求
//            Request request = new Request.Builder().get()
//                    .url(url)
//                    .build();
//
//            //3.发起请求
//            Response response = okHttpClient.newCall(request).execute();
//
//            //4.取出结果
//            String resultJsonString = response.body().string();
//
//            LogUtils.i("resultJsonString:" + resultJsonString);
//
//            //解析json
//            Gson gson = new Gson();
//            HomeBean homeBean = gson.fromJson(resultJsonString,HomeBean.class);
//
//            if(homeBean != null) {
//                return homeBean.list;
//            }
//
//            return super.onLoadMore();
//        }
//    }
//}