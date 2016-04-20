package com.wz.googleplay.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wz.googleplay.factory.ThreadPoolProxyFactory;
import com.wz.googleplay.holder.GameHolder;
import com.wz.googleplay.holder.LoadMoreHolder;
import com.wz.googleplay.utils.UIUtils;

import java.util.List;

/**
 * Created by wz on 2016/4/17.
 *
 * @描述 在MyBaseAdapter基础之上, 对我们getView以及ViewHolder进行整合封装
 */
public abstract class SuperBaseAdapter<ITEMBEANTYPE> extends MyBaseAdapter implements AdapterView.OnItemClickListener {

    private static final int VIEWTYPE_LOADMORE = 0;
    private static final int VIEWTYPE_NORMAL = 1;
    private final AbsListView mAbsListView;
    private LoadMoreHolder mLoadMoreHolder;
    private LoadMoreTask mLoadMoreTask;

    public SuperBaseAdapter(AbsListView absListView, List DataSource) {
        super(DataSource);
        mAbsListView = absListView;
        mAbsListView.setOnItemClickListener(this);
    }

    /*--------------- ListView中展示几种类型ViewType ---------------*/


    /**
     * 1.复写BaseAdapter中的2个方法
     * 1.在getView中根据不同的类型分别处理
     *
     */

    /**
     * get(得到) ViewType Count(总数)
     *
     * @return
     */
    @Override
    public int getViewTypeCount() {
        //  return super.getViewTypeCount(); //默认是1
        return 2;// 1(普通条目)+1(加载更多) = 2;
    }

    /**
     * get(得到) Item(指定条目)ViewType (int position)
     *
     * @param position
     * @return
     */
    /**
     * Integers must be in the range
     * 0   ==>0
     * to
     * getViewTypeCount() - 1==>1
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (position == getCount() - 1) { //滑到了最后一条
            //加载更多
            return VIEWTYPE_LOADMORE; //0
        } else {
            //普通条目
            return getNormalItemViewType(position); //1
        }
        //     return super.getItemViewType(position);//默认是0
    }

    /**
     * @des 决定普通的item是否有更多的ViewType类型,默认是1
     * @des 子类可以通过覆写该方法,返回2个种以上ViewType
     * @call 对应的listView实际情况有2种以上的ViewType的时候就需要覆写该方法
     * @return
     * @param position
     */
    public  int getNormalItemViewType(int position) {
        return VIEWTYPE_NORMAL;//默认返回是1
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*--------------- 决定根视图 ---------------*/
        BaseHolder baseHolder = null;
        if (convertView == null) {
            //返回BaseHolder子类实例对象
            //根据当前条目具体类型，返回具体的跟视图
            if (getItemViewType(position) == VIEWTYPE_LOADMORE) { //返回加载更多的视图
                baseHolder = getmLoadMoreHolder();

            } else {// 返回普通的视图
                //返回BaseHolder子类实例对象
                baseHolder = getSpecialHolder(position);
            }
        } else {
            baseHolder = (BaseHolder) convertView.getTag();
        }
        /*--------------- 视图和数据的绑定 ---------------*/
        if (getItemViewType(position) == VIEWTYPE_LOADMORE) {
            // 触发加载更多的数据
            if (hasLoadMore()) {
                //触发加载更多的数据
                triggerLoadMoreData();
            } else {// 子类覆写了hasLoadMore,表明没有加载更多
                mLoadMoreHolder.setDataAndRefreshHolderView(LoadMoreHolder.LOADMORE_NONE);
            }
        } else {
            baseHolder.setDataAndRefreshHolderView(mDataSource.get(position));
        }

        return baseHolder.mRootView;
    }

    /**
     * @return
     * @des 返回BaseHolder子类实例对象
     * @des 必须实现, 但是不知道具体实现, 定义成为抽象方法, 交给自子类具体实现
     * @call 走到getView方法中而且(convertView == null)的时候被调用
     * @param position
     */
    public abstract BaseHolder getSpecialHolder(int position);

    /**
     * d得到BaseHolder的子类的实例化对象(LoadMoreHolder的实例化对象)
     * 因为LoadMoreHolder具体知道是啥，所以就在这个地方直接初始化
     *
     * @return
     */
    private LoadMoreHolder getmLoadMoreHolder() {
        if (mLoadMoreHolder == null) {
            mLoadMoreHolder = new LoadMoreHolder();
        }
        return mLoadMoreHolder;
    }

    /**
     * @return
     * @des 决定是否有加载更多
     * @des 子类可以覆写hasLoadMore修改是否有加载更多
     */
    public boolean hasLoadMore() {
        return true; //默认情况有加载更多
    }


    /**
     * @des 触发异步加载更多的数据
     * @call 滑到底部，而且有加载更多的时候被调用
     */
    private void triggerLoadMoreData() {

        // 如果真正加载更多,就不触发加载-->可以通过判断任务是否执行完成了
        if (mLoadMoreTask == null) {
            // 刷新loadmore的ui
            int state = LoadMoreHolder.LOADMORE_LOADING;
            mLoadMoreHolder.setDataAndRefreshHolderView(state);
            mLoadMoreTask = new LoadMoreTask();
            ThreadPoolProxyFactory.createNormalThreadPoolProxy().execute(new LoadMoreTask());
        }

    }

    class LoadMoreTask implements Runnable {
        private static final int PAGERSIZE = 20;

        @Override
        public void run() {
            /*--------------- 定义两个影响最后ui的数据 ---------------*/
            int state = 0;
            List<ITEMBEANTYPE> loadMoreList = null;

            /*--------------- 完成真正的加载更多,修改对应上面的2个数据 ---------------*/
            // 真正的加载更多的数据
            try {
                loadMoreList = onLoadMore();
                if (loadMoreList == null) {
                    state = LoadMoreHolder.LOADMORE_NONE; //没有加载更多
                } else {
                    if (loadMoreList.size() < PAGERSIZE) {// 没有加载更多
                        state = LoadMoreHolder.LOADMORE_NONE;
                    } else { //=20
                        state = LoadMoreHolder.LOADMORE_LOADING;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                state = LoadMoreHolder.LOADMORE_ERROR;
            }

            /*--------------- 创建临时变量,保存我们的数据 ---------------*/
            final List<ITEMBEANTYPE> finalLoadMoreList = loadMoreList;
            final int finalState = state;

            /*--------------- 根据修改之后的两个数据完成ui的刷新操作 ---------------*/
            UIUtils.postTaskSafely(new Runnable() {
                @Override
                public void run() {
                    //刷新ui
                    // 刷新ui-->listview -- >list
                    if (finalLoadMoreList != null) {
                        //追加到已有的dataset
                        mDataSource.addAll(finalLoadMoreList);
                        //刷新listview
                        notifyDataSetChanged();
                        ;
                    }
                    // 刷新ui-->loadmore(没有加载更多,加载更多失败)-->Integer
                    mLoadMoreHolder.setDataAndRefreshHolderView(finalState);
                }
            });
            mLoadMoreTask = null;
        }
    }

    /**
     * @des 真正的在子类线程中加载更多
     * @des 选择性的实现
     * @des 加载更多的时候可能出现异常
     * @return
     * @throws Exception
     */
    /**
     * 抛出的异常抛到哪里去了?-->方法的调用出
     * 什么时候在方法定义的时候需要抛出异常?
     * 在方法的调用出,会根据具体异常信息产生不同的逻辑判断时候(异常有决定逻辑的时候)
     */
    public List<ITEMBEANTYPE> onLoadMore() throws Exception {
        return null;
    }

    /*--------------- 处理item的点击事件 ---------------*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //一个position处理的细节
        if (mAbsListView instanceof ListView) {
            position = position - ((ListView) mAbsListView).getHeaderViewsCount();
        }
        // 处理点击事件-->到底是点击的加载更多还是点击的普通条目
        if (getItemViewType(position) == VIEWTYPE_LOADMORE) {
            if (mLoadMoreTask == null) {//一个任务执行完成了.
                // 触发重新加载更多
                triggerLoadMoreData();
            }
        } else {
            onNormalItemClick(parent, view, position, id);
        }

    }

    /**
     * @des 普通条目的点击事件
     * @des 如果子类需要处理普通条目的点击事件,就覆写该方法即可
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    public void onNormalItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 默认是没有实现
    }

}