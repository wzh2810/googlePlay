package com.wz.googleplay.factory;

import com.wz.googleplay.manager.ThreadPoolProxy;

/**
 * Created by wz on 2016/4/17.
 * @描述	      创建普通的线程池的代理
 * @描述	      创建下载的线程池的代理
 */
public class ThreadPoolProxyFactory {
    static ThreadPoolProxy mNormalThreadPoolProxy; //只需创建一次即可
    static ThreadPoolProxy mDownloadThreadpoolProxy; //只需创建一次即可

    /**
     * 返回普通线程池的代理
     * 双重检查加锁，保证只有第一次实例化的时候才启动用同步机制，提高效率
     * @return
     */
    public static ThreadPoolProxy createNormalThreadPoolProxy() {
        if(mNormalThreadPoolProxy == null) {
            synchronized (ThreadPoolProxyFactory.class) {
                if(mNormalThreadPoolProxy == null) {
                    mNormalThreadPoolProxy = new ThreadPoolProxy(5,5,3000);
                }
            }
        }

        return mNormalThreadPoolProxy;
    }

    /**
     * 返回下载线程池
     */
    public static ThreadPoolProxy createDownloadThreadPoolProxy() {
        if(mDownloadThreadpoolProxy == null) {
            synchronized (ThreadPoolProxyFactory.class) {
                if(mDownloadThreadpoolProxy == null) {
                    mDownloadThreadpoolProxy = new ThreadPoolProxy(3,3,3000);
                }
            }
        }
        return mDownloadThreadpoolProxy;
    }
}
