package com.example.a18302.guigu_news.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.a18302.guigu_news.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RefreshListview extends ListView {
    /**
     * 下拉刷新
     */
    private View ll_pull_down_refresh;
    private ImageView iv_arrow;
    private ProgressBar pb_status;
    private TextView tv_status;
    private TextView tv_time;

    /**
     * 下拉刷新的高
     */
    private int pullDownRefresh;

    /**
     * 下拉刷新和顶部轮播图
     *
     * @param context
     */
    private LinearLayout headerView;

    /**
     * 下拉刷新
     */
    public static final int PULL_DOWN_REFRESH = 0;

    /**
     * 手松刷新
     */
    public static final int RELEAS_REFRESH = 1;

    /**
     * 正在刷新
     */
    public static final int REFRESHING = 2;

    private int currentstatus = PULL_DOWN_REFRESH;

    private Animation upAnimation;
    private Animation downAnimation;

    /**
     * 加载更多的
     *
     * @param context
     */
    private View footView;
    /**
     * 加载更多 控件的高
     */
    private int footerViewHeight;
    private boolean isLoadMore = false;

    /**
     * 顶部轮播图
     *
     * @param context
     */
    private View topnewsView;
    /**
     * listview在Y轴上的坐标
     */
    private int listviewOnScrennY = -1;

    public RefreshListview(Context context) {
        this(context, null);
    }

    public RefreshListview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView(context);
        initAnimation();
        initFooterView(context);
    }

    private void initFooterView(Context context) {
        footView = View.inflate(context, R.layout.refresh_footer, null);
        footView.measure(0, 0);
        footerViewHeight = footView.getMeasuredHeight();

        footView.setPadding(0, -footerViewHeight, 0, 0);

        //listview添加到footer
        addFooterView(footView);

        //监听Listview滑动到最后一条的监听
        setOnScrollListener(new MyOnScrollListener());
    }

    /**
     * 添加顶部轮播图
     *
     * @param topnewsView
     */
    public void addTopNewsView(View topnewsView) {
        if (topnewsView != null) {
            this.topnewsView = topnewsView;
            headerView.addView(topnewsView);
        }
    }

    /**
     * 是否完全显示顶部轮播图
     * 当listview在屏幕上的Y轴坐标小于或者等于顶部轮播图在Y轴的坐标的死后，顶部轮播图完全显示
     *
     * @return
     */
    public boolean isDisplayTopNews() {

        if (topnewsView != null) {
            //1.得到listview在屏幕上的坐标
            int[] location = new int[2];
            if (listviewOnScrennY == -1) {
                getLocationOnScreen(location);
                listviewOnScrennY = location[1];
            }
            //2.得到顶部轮播图在屏幕上的坐标
            topnewsView.getLocationOnScreen(location);
            int topNewsViewOnScreenY = location[1];

//        if (listviewOnScrennY <= topNewsViewOnScreenY) {
//            return true;
//        } else {
//            return false;
//        }

            return listviewOnScrennY <= topNewsViewOnScreenY;
        } else {
            return true;
        }
    }


    class MyOnScrollListener implements OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {
            //当静止或者惯性滚动的时候
            if (i == OnScrollListener.SCROLL_STATE_IDLE || i == OnScrollListener.SCROLL_STATE_FLING) {
                //并且是最后一条可见
                if (getLastVisiblePosition() >= getCount() - 1) {
                    //1.显示加载更多的布局
                    footView.setPadding(8, 8, 8, 8);
                    //2.状态改变
                    isLoadMore = true;
                    //3.回调接口
                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onLoadMore();
                    }
                }
            }


        }

        @Override
        public void onScroll(AbsListView absListView, int i, int i1, int i2) {

        }
    }

    private void initAnimation() {
        upAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true);

        downAnimation = new RotateAnimation(-180, -360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        downAnimation.setDuration(500);
        downAnimation.setFillAfter(true);
    }

    private void initHeaderView(Context context) {
        headerView = (LinearLayout) View.inflate(context, R.layout.refresh_header, null);
        //下拉刷新控件
        ll_pull_down_refresh = headerView.findViewById(R.id.ll_pull_down_refresh);
        iv_arrow = headerView.findViewById(R.id.iv_arrow);
        pb_status = headerView.findViewById(R.id.pb_status);
        tv_status = headerView.findViewById(R.id.tv_status);
        tv_time = headerView.findViewById(R.id.tv_time);

        //测量
        ll_pull_down_refresh.measure(0, 0);
        pullDownRefresh = ll_pull_down_refresh.getMeasuredHeight();
        //默认隐藏下拉刷新控件

        ll_pull_down_refresh.setPadding(0, -pullDownRefresh, 0, 0);


        //添加listview头
        RefreshListview.this.addHeaderView(headerView);
    }

    private float startY = -1;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {
                    startY = ev.getY();
                }

                //判断顶部轮播图是否完全显示，只有完全显示才会有下拉刷新
                boolean isDisplayTopNews = isDisplayTopNews();
                if (!isDisplayTopNews) {
                    //加载更多
                    break;
                }


                //如果是正在刷新，就不再让刷新了
                if (currentstatus == REFRESHING) {
                    break;
                }

                float endY = ev.getY();
                //滑动的距离
                float distanceY = endY - startY;
                if (distanceY > 0) {
                    int paddingTop = (int) (-pullDownRefresh + distanceY);

                    if (paddingTop < 0 && currentstatus != PULL_DOWN_REFRESH) {
                        currentstatus = pullDownRefresh;
                        //更新状态
                        refreshViewState();

                    } else if (paddingTop > 0 && currentstatus != RELEAS_REFRESH) {
                        currentstatus = RELEAS_REFRESH;
                        //更新状态
                        refreshViewState();
                    }

                    ll_pull_down_refresh.setPadding(0, paddingTop, 0, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if (currentstatus == PULL_DOWN_REFRESH) {
                    ll_pull_down_refresh.setPadding(0, -pullDownRefresh, 0, 0);

                } else if (currentstatus == RELEAS_REFRESH) {

                    currentstatus = REFRESHING;
                    refreshViewState();
                    ll_pull_down_refresh.setPadding(0, 0, 0, 0);
                    //回调接口

                    if (mOnRefreshListener != null) {
                        mOnRefreshListener.onPullDownRefresh();
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void refreshViewState() {
        switch (currentstatus) {
            case PULL_DOWN_REFRESH:
                iv_arrow.startAnimation(downAnimation);
                tv_status.setText("下拉刷新...");
                break;
            case RELEAS_REFRESH:
                iv_arrow.startAnimation(upAnimation);
                tv_status.setText("手松刷新...");

                break;
            case REFRESHING:
                tv_status.setText("正在刷新...");
                pb_status.setVisibility(VISIBLE);
                iv_arrow.clearAnimation();
                iv_arrow.setVisibility(GONE);
                break;
        }

    }

    /**
     * 当联网成功和失败的时候，回调该方法
     * 用户刷新状态的还原
     *
     * @param sucess
     */
    public void onRefreshFinish(boolean sucess) {
        if (isLoadMore) {
            //加载更多
            isLoadMore = false;
            //隐藏加载更多的布局
            footView.setPadding(0, -footerViewHeight, 0, 0);
        } else {
            //下拉刷新
            tv_status.setText("下拉刷新...");
            currentstatus = PULL_DOWN_REFRESH;
            iv_arrow.clearAnimation();
            pb_status.setVisibility(GONE);
            iv_arrow.setVisibility(VISIBLE);

            //隐藏下拉刷新控件
            ll_pull_down_refresh.setPadding(0, -pullDownRefresh, 0, 0);
            if (sucess) {
                //设置最新的更新时间
                tv_time.setText("上次跟新时间：" + getSystemTime());
            }
        }
    }

    /**
     * 得到当前系统的时间
     *
     * @return
     */
    public String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    /**
     * 监听控件的刷新
     */
    public interface OnRefreshListener {
        /**
         * 当下拉刷新的时候回调这个方法
         */
        public void onPullDownRefresh();

        /**
         * 当加载更多的时候回调这个方法
         */
        public void onLoadMore();
    }

    private OnRefreshListener mOnRefreshListener;

    /**
     * 设置监听刷新，由外界设置
     */
    public void setOnOnRefreshListener(OnRefreshListener l) {
        this.mOnRefreshListener = l;
    }

}
