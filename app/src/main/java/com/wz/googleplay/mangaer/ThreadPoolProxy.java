package com.wz.googleplay.mangaer;

import java.io.InterruptedIOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by wz on 2016/4/17.
 * @dec     ThreadPool的代理
 * @dec     帮助ThreadPool做一些事情，进行封装，暴露用户真正关心得方法
 * @dec     如果用户想使用线程池，真正关心的无非就是（提交任务，执行任务，移除任务）
 */
public class ThreadPoolProxy {

    int  mCorePoolSize;
    int  mMaximumPoolSize;
    long mKeepAliveTime;

    ThreadPoolExecutor mThreadPoolExecutor;//只需要初始化一次

    public ThreadPoolProxy(int CorePoolSize,int MaximumPoolSize,long KeepAliveTime) {
        mCorePoolSize = CorePoolSize;
        mMaximumPoolSize = MaximumPoolSize;
        mKeepAliveTime = KeepAliveTime;
    }

    private void initThreadPoolExecutor() {
        if(mThreadPoolExecutor == null || mThreadPoolExecutor.isShutdown() || mThreadPoolExecutor.isTerminated()) {
            synchronized (ThreadPoolProxy.class) {
                if(mThreadPoolExecutor == null || mThreadPoolExecutor.isShutdown() || mThreadPoolExecutor.isTerminated()) {
                    TimeUnit unit = TimeUnit.MILLISECONDS;
                    BlockingDeque<Runnable> workQueue = new LinkedBlockingDeque<>();
                    ThreadFactory threadFactory = Executors.defaultThreadFactory();
                    RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy()   ;
                    mThreadPoolExecutor = new ThreadPoolExecutor(
                            mCorePoolSize, //核心池的大小
                            mMaximumPoolSize, //最大线程数
                            mKeepAliveTime,//保持时间
                            unit,//保持时间的单位
                            workQueue,//工作队列
                            threadFactory,//线程工厂
                            handler//异常捕获器
                    );
                }
            }
        }
    }

    /**
     * 提交任务和执行任务的区别
     * 是否有返回值?
     *      提交任务有返回值
     *      执行任务就没有返回值
     *     提交任务的返回值有什么用?Future对象有什么用?
     1.有提供方法去检测任务是否执行完成
     2.有提供方法去等待任务的完成
     3.有提供方法去收到任务执行之后的结果 通过get方法 cancel
     关心Future 里里面的get方法
     get是一个阻塞方法,抛出异常,返回值正好是任务执行之后返回的结果
     get可以try catch任务执行过程中可能抛出的异常信息
     *
     */



    /**
     * 提交任务
     */
    public Future<?> submit(Runnable task) {
        initThreadPoolExecutor();
        Future<?> submitResult = mThreadPoolExecutor.submit(task);
        return submitResult;
    }

    /**
     * 执行任务
     */
    public  void execute(Runnable task) {
        initThreadPoolExecutor();
        mThreadPoolExecutor.execute(task);
    }

    /**
     * 移除任务
     */
    public void remove(Runnable task) {
        initThreadPoolExecutor();
        mThreadPoolExecutor.remove(task);
    }
}
