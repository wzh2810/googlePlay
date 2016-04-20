package com.wz.googleplay.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.wz.googleplay.R;
import com.wz.googleplay.factory.ThreadPoolProxyFactory;

import com.wz.googleplay.utils.LogUtils;
import com.wz.googleplay.utils.UIUtils;

/**
 * Created by wz on 2016/4/16.
 *
 * @描述 1.数据加载
 * @描述 2.视图的展示逻辑(加载视图, 空视图, 错误视图, 成功视图)
 * @描述 3.为什么方法.直接继承FrameLayout, 变成4种视图的容器
 * @描述	 4.控制一个大的流程,大的逻辑,不关心具体的细节实现
 */
public abstract class LoadingPager extends FrameLayout {

    private View			mLoadingView;
    private View			mErrorView;
    private View			mEmptyView;
    private View			mSuccessView;

    public static final int	STATE_NONE		= -1;				// 默认视图-->显示一个加载的效果
    public static final int	STATE_LOADING	= 0;				// 加载中-->表示正在加载数据
    public static final int	STATE_EMPTY		= 1;				// 空视图
    public static final int	STATE_ERROR		= 2;				// 错误视图
    public static final int	STATE_SUCCESS	= 3;				// 成功视图

    public int				mCurState		= STATE_NONE;	// 默认显示loading视图

    public LoadingPager(Context context) {
        super(context);
        initCommonView();
    }

    /**
     * @des 初始化常规视图(加载视图, 空视图, 错误视图), 因为他们相对比较静态
     * @call LoadingPager初始化的时候被调用
     */
    private void initCommonView() {
        // 加载视图
        mLoadingView = View.inflate(UIUtils.getContext(), R.layout.pager_loading, null);
        addView(mLoadingView);

        // 错误页面
        mErrorView = View.inflate(UIUtils.getContext(), R.layout.pager_error, null);
        addView(mErrorView);


        mErrorView.findViewById(R.id.error_btn_retry).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //希望重新的触发加载数据
                triggerLoadData();
            }
        });


        // 空页面
        mEmptyView = View.inflate(UIUtils.getContext(), R.layout.pager_empty, null);
        addView(mEmptyView);

        // 根据当前的状态,显示其中某一个视图
        refreshUIByState();
    }

    /**
     * @des 根据当前的状态, 显示其中某一个视图
     * @call 1.LoadingPager初始化的时候被调用
     * @call 2.数据开始加载之前,显示加载中的视图
     * @call 3.数据加载完成,根据具体结果,展示不同的数据
     */
    private void refreshUIByState() {
        // 控制loading视图的显示/隐藏
        mLoadingView.setVisibility((mCurState == STATE_LOADING)||(mCurState == STATE_NONE) ? View.VISIBLE : View.GONE);

        // 控制empty视图的显示/隐藏
        mEmptyView.setVisibility((mCurState == STATE_EMPTY) ? View.VISIBLE : View.GONE);

        // 控制错误视图的显示/隐藏
        mErrorView.setVisibility((mCurState == STATE_ERROR) ? View.VISIBLE : View.GONE);

        // 如果是数据加载完成之后来到这里,那么就可能有成功视图了吧.
        if (mCurState == STATE_SUCCESS && mSuccessView == null) {
            // 初始化成功视图
            mSuccessView = initSuccessView();
            // 加入容器
            addView(mSuccessView);
        }

        // 控制成功视图的显示/隐藏
        if (mSuccessView != null) {
            mSuccessView.setVisibility((mCurState == STATE_SUCCESS) ? View.VISIBLE : View.GONE);
        }

    }

    /**
     //页面(Fragment/activity)显示分析
     //Fragment共性-->页面共性-->视图的展示
     /**
     任何应用其实就只有4种页面类型
     ① 加载页面
     ② 错误页面
     ③ 空页面

     ④ 成功页面-->具体的,复杂一些
     ①②③三种页面一个应用基本是固定(静态的)的
     每一个fragment对应的页面④就不一样
     进入应用的时候显示①,②③④需要加载数据之后才知道显示哪个
     */

	/*############### 数据加载相关的逻辑 ###############*/

    // 数据加载的流程
    /**
     ① 触发加载  	进入页面开始加载/点击某一个按钮的时候加载
     ② 异步加载数据  -->显示加载视图
     ③ 处理加载结果
     ① 成功-->显示成功视图
     ② 失败
     ① 数据为空-->显示空视图
     ② 数据加载失败-->显示加载失败的视图(重试操作)
     */
    /**
     * @des 触发进行数据的加载
     * @call 想要加载数据的时候调用此方法
     */
    public void triggerLoadData() {
        // 如果当前状态是成功状态就无需加载
        if (mCurState != STATE_SUCCESS && mCurState != STATE_LOADING) {
            // 重置mCurState为loading状态
            mCurState = STATE_LOADING;
            // 根据当前的状态刷新ui
            refreshUIByState();

            LogUtils.s("###触发加载数据了");
            // ① 触发加载
            // ② 异步加载数据
//			new Thread(new LoadDataTask()).start();
            ThreadPoolProxyFactory.createNormalThreadPoolProxy().execute(new LoadDataTask());
        }
    }

    class LoadDataTask implements Runnable {
        @Override
        public void run() {
            // 真正的在子线程里面开始加载想加载的数据
            LoadedResult tempState = initData();

            // 处理网络加载之后的结果
            mCurState = tempState.getState();

            UIUtils.postTaskSafely(new Runnable() {
                @Override
                public void run() {
                    // 刷新ui
                    refreshUIByState();
                }
            });
        }
    }

	/*############### loadingPager中定义两个抽象方法 ###############*/

    /**
     * @return
     * @des 真正的在子线程里面开始加载想加载的数据
     * @des 不知道具体如何加载数据
     * @des 必须实现, 但是不知道具体实现, 定义成为抽象方法, 交给子类具体实现
     * @call 想要加载数据的时候
     */
    public abstract LoadedResult initData();

    /**
     * @return
     * @des 展示具体的成功视图
     * @des 此时不知道具体成功视图是啥
     * @des 必须实现, 但是不知道具体实现, 定义成为抽象方法, 交给子类具体实现
     * @call 数据加载完成, 并且是数据加载成功的时候
     */
    public abstract View initSuccessView();

    /*############### 定义枚举,控制数据加载完成之后的返回值只能在(1,2,3)中的某一个 ###############*/
    public enum LoadedResult {
        EMPTY(STATE_EMPTY), ERROR(STATE_ERROR), SUCCESS(STATE_SUCCESS);
        int	state;

        public int getState() {
            return state;
        }

        private LoadedResult(int state) {
            this.state = state;
        }
    }
}
abstract class LoadingPager1 extends FrameLayout {

    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;
    private View mSuccessView;

    public static final int STATE_NONE = -1; //默认视图-->显示一个加载的效果
    public static final int STATE_LOADING = 0;                // 加载中-->表示正在加载数据
    public static final int STATE_EMPTY = 1;                // 空视图
    public static final int STATE_ERROR = 2;                // 错误视图
    public static final int STATE_SUCCESS = 3;                // 成功视图

    public int mCurState = STATE_NONE; //默认显示loading视图

    public LoadingPager1(Context context) {
        super(context);
        initCommomView();
    }

    /**
     * @des 初始化常规视图(加载视图, 空视图, 错误视图), 因为他们相对比较静态
     * @call LoadingPager初始化的时候被调用
     */
    private void initCommomView() {
        //加载视图
        mLoadingView = View.inflate(UIUtils.getContext(), R.layout.pager_loading, null);
        addView(mLoadingView);

        //错误视图
        mErrorView = View.inflate(UIUtils.getContext(), R.layout.pager_error, null);
        addView(mErrorView);

//        mErrorView.findViewById(R.id.error_btn_retry).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //希望重新的触发加载数据
//                triggerLoadData();
//            }
//        });

        //空页面
        mEmptyView = View.inflate(UIUtils.getContext(), R.layout.pager_empty, null);
        addView(mEmptyView);

        //根据当前的状态，显示其中某一个视图
        refreshUIByState();
    }

    /**
     * @dec 根据当前的状态，显示其中某一个视图
     * @call 1.loadingPager初始化的时候被调用
     * @call 2.数据加载完成的时候
     */
    private void refreshUIByState() {
        // 控制loading视图的显示/隐藏
        mLoadingView.setVisibility((mCurState == STATE_LOADING)||(mCurState == STATE_NONE) ? View.VISIBLE : View.GONE);

        // 控制empty视图的显示/隐藏
        mEmptyView.setVisibility((mCurState == STATE_EMPTY) ? View.VISIBLE : View.GONE);

        // 控制错误视图的显示/隐藏
        mErrorView.setVisibility((mCurState == STATE_ERROR) ? View.VISIBLE : View.GONE);

        // 如果是数据加载完成之后来到这里,那么就可能有成功视图了吧.
        if (mCurState == STATE_SUCCESS && mSuccessView == null) {
            // 初始化成功视图
            mSuccessView = initSuccessView();
            // 加入容器
            addView(mSuccessView);
        }

        // 控制成功视图的显示/隐藏
        if (mSuccessView != null) {
            mSuccessView.setVisibility((mCurState == STATE_SUCCESS) ? View.VISIBLE : View.GONE);
        }

    }
    private void refreshUIByState1() {
        // 控制loading视图的显示/隐藏
        mLoadingView.setVisibility((mCurState == STATE_LOADING) ||(mCurState == STATE_NONE) ? View.VISIBLE : View.GONE);

        //控制empty视图的显示/隐藏
        mEmptyView.setVisibility((mCurState == STATE_EMPTY) ? View.VISIBLE : View.GONE);

        //控制error视图的显示/隐藏
        mEmptyView.setVisibility((mCurState == STATE_ERROR) ? View.VISIBLE : View.GONE);

        //如果是数据加载完成之后来到这里，那么久可能有成功视图
        if (mCurState == STATE_SUCCESS && mSuccessView == null) {
            //初始化成功视图
            mSuccessView = initSuccessView();
            //加入到容器
            addView(mSuccessView);
        }

        //控制成功视图的显示/隐藏
        if (mSuccessView != null) {
            mSuccessView.setVisibility((mCurState == STATE_SUCCESS) ? View.VISIBLE : View.GONE);
        }
    }

    /**
     //页面(Fragment/activity)显示分析
     //Fragment共性-->页面共性-->视图的展示
     /**
     任何应用其实就只有4种页面类型
     ① 加载页面
     ② 错误页面
     ③ 空页面

     ④ 成功页面-->具体的,复杂一些
     ①②③三种页面一个应用基本是固定(静态的)的
     每一个fragment对应的页面④就不一样
     进入应用的时候显示①,②③④需要加载数据之后才知道显示哪个
     */

	/*############### 数据加载相关的逻辑 ###############*/

    // 数据加载的流程
    /**
     ① 触发加载  	进入页面开始加载/点击某一个按钮的时候加载
     ② 异步加载数据  -->显示加载视图
     ③ 处理加载结果
     ① 成功-->显示成功视图
     ② 失败
     ① 数据为空-->显示空视图
     ② 数据加载失败-->显示加载失败的视图(重试操作)
     */
    /**
     * @des 触发进行数据的加载
     * @call 想要加载数据的时候调用此方法
     */

    public void triggerLoadData() {
        //如果当前状态是成功状态就无需加载
        if (mCurState != STATE_SUCCESS && mCurState != STATE_LOADING) {
                //重置mCurState为loading状态
                mCurState = STATE_LOADING;
                //根据当前的状态刷新ui
                refreshUIByState();
                LogUtils.s("###触发加载数据了");

                // ① 触发加载
                // ② 异步加载数据
       //         new Thread(new LoadDataTask()).start();
            ThreadPoolProxyFactory.createNormalThreadPoolProxy().execute(new LoadDataTask());
        }
    }

    class LoadDataTask implements Runnable {

        @Override
        public void run() {
            // 真正的在子线程里面开始加载想加载的数据
            LoadedResult tempState = initData();

            //处理网络加载之后的结果
            mCurState = tempState.getState();

            UIUtils.postTaskSafely(new Runnable() {
                @Override
                public void run() {
                    //刷新ui
                    refreshUIByState();
                }
            });

        }
    }

    /*############### loadingPager中定义两个抽象方法 ###############*/

    /**
     * @return
     * @des 真正的在子线程里面开始加载想加载的数据
     * @des 不知道具体如何加载数据
     * @des 必须实现, 但是不知道具体实现, 定义成为抽象方法, 交给子类具体实现
     * @call 想要加载数据的时候
     */
    public abstract  LoadedResult initData();

    /**
     * @return
     * @des 展示具体的成功视图
     * @des 此时不知道具体成功视图是啥
     * @des 必须实现, 但是不知道具体实现, 定义成为抽象方法, 交给子类具体实现
     * @call 数据加载完成, 并且是数据加载成功的时候
     */
    public abstract View initSuccessView();

    /*############### 定义枚举,控制数据加载完成之后的返回值只能在(1,2,3)中的某一个 ###############*/
    public enum LoadedResult {
        EMPTY(STATE_EMPTY),ERROR(STATE_ERROR),SUCCESS(STATE_SUCCESS);
        int state;
        public int getState() {
            return state;
        }

        private LoadedResult(int state) {
            this.state = state;
        }
    }
}


