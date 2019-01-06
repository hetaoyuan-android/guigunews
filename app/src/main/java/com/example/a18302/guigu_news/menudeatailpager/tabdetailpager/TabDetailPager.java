package com.example.a18302.guigu_news.menudeatailpager.tabdetailpager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a18302.guigu_news.R;
import com.example.a18302.guigu_news.activity.NewsDetailActivity;
import com.example.a18302.guigu_news.base.MenuDetailBasePager;
import com.example.a18302.guigu_news.domain.NewsCenterPagerBean2;
import com.example.a18302.guigu_news.domain.TabDetailPagerBean;
import com.example.a18302.guigu_news.utils.CacheUtils;
import com.example.a18302.guigu_news.utils.Contants;
import com.example.a18302.guigu_news.view.HorizontalScrollViewpager;
import com.example.a18302.guigu_news.view.RefreshListview;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

/**
 * 页签页面
 */
public class TabDetailPager extends MenuDetailBasePager {
    public static final String READ_ARRAY_ID = "read_array_id";
    private final NewsCenterPagerBean2.DetailPagerData.ChildrenData childrenData;

    private String url;

    private HorizontalScrollViewpager viewPager;
    private TextView tv_title;
    private LinearLayout ll_point_group;
    private RefreshListview listView;

    //之前高亮的位置
    private int position;

    //顶部轮播图
    private List<TabDetailPagerBean.DataEntity.TopnewsData> topnews;
    //新闻列表的胡数据集合
    private List<TabDetailPagerBean.DataEntity.NewsData> news;

    private TabDetailPagerListAdapter tabDetailPagerListAdapter;

    private ImageOptions imageOptions;

    private String moreUrl;
    private boolean isLoadMore = false;

    private InternalHandler internalHandler;

    public TabDetailPager(Context context, NewsCenterPagerBean2.DetailPagerData.ChildrenData childrenData) {
        super(context);
        this.childrenData = childrenData;
        imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(100), DensityUtil.dip2px(100))
                .setRadius(DensityUtil.dip2px(5))
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.drawable.news_pic_default)
                .setFailureDrawableId(R.drawable.news_pic_default)
                .build();
    }

    @Override
    protected View initView() {
        View view = View.inflate(context, R.layout.tabdetail_pager, null);
        listView = view.findViewById(R.id.listview);

        View topnewsView = View.inflate(context, R.layout.topnews, null);
        viewPager = topnewsView.findViewById(R.id.viewpager);
        tv_title = topnewsView.findViewById(R.id.tv_title);
        ll_point_group = topnewsView.findViewById(R.id.ll_point_group);

        //把顶部轮播图以头的方式添加到listview中
//        listView.addHeaderView(topnewsView);
        listView.addTopNewsView(topnewsView);

        //设置监听下拉刷新
        listView.setOnOnRefreshListener(new MyOnRefreshListener());

        //设置listview的item监听事件
        listView.setOnItemClickListener(new MyOnItemClickListener());

        return view;
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            int realPosition = i - 1;
            TabDetailPagerBean.DataEntity.NewsData newsData = news.get(realPosition);

            //1.取出保存的id集合
            String idArray = CacheUtils.getString(context, READ_ARRAY_ID);  //11,22,33,34,35
            //2.判断是否存在，如果不存在，才保存，并且刷新适配器
            if (!idArray.contains(newsData.getId() + "")) {
                CacheUtils.putString(context, READ_ARRAY_ID, idArray + newsData.getId() + ",");
                //刷新适配器
                tabDetailPagerListAdapter.notifyDataSetChanged();
            }

            //跳转到新闻浏览页面
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra("url", Contants.BASE_URL + newsData.getUrl());
            context.startActivity(intent);


        }
    }


    public void getMoreDataFromNet() {
        RequestParams params = new RequestParams(moreUrl);
        params.setConnectTimeout(4000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                listView.onRefreshFinish(false);
                isLoadMore = true;
                //解析数据
                ProcessData(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    class MyOnRefreshListener implements RefreshListview.OnRefreshListener {

        @Override
        public void onPullDownRefresh() {
            getDataFromNet();
        }

        @Override
        public void onLoadMore() {
            if (TextUtils.isEmpty(moreUrl)) {
                listView.onRefreshFinish(false);
            } else {
                getMoreDataFromNet();
            }
        }
    }

    @Override
    public void initData() {
        super.initData();
        url = Contants.BASE_URL + childrenData.getUrl();
        //把之前的缓存数据取出
        String saveJson = CacheUtils.getString(context, url);
        if (!TextUtils.isEmpty(saveJson)) {
            //解析和显示处理的数据
            ProcessData(saveJson);
        }
        //联网请求数据
        getDataFromNet();
    }

    private void ProcessData(String saveJson) {
        TabDetailPagerBean bean = parseJson(saveJson);
        LogUtil.e(childrenData.getTitle() + "解析成功" + bean.getData().getNews().get(0).getTitle());

        moreUrl = "";
        if (TextUtils.isEmpty(bean.getData().getMore())) {
            moreUrl = "";
        } else {
            moreUrl = Contants.BASE_URL + bean.getData().getMore();
        }

        //默认
        if (!isLoadMore) {
            //顶部轮播图
            topnews = bean.getData().getTopnews();
            //viewpager的适配器
            viewPager.setAdapter(new TabDetailPagerTopNewsAdapter());

            //移除所有的圆点
            ll_point_group.removeAllViews();

            for (int i = 0; i < topnews.size(); i++) {
                ImageView imageView = new ImageView(context);
                imageView.setBackgroundResource(R.drawable.point_selector);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(8), DensityUtil.dip2px(8));

                if (i == 0) {
                    imageView.setEnabled(true);
                } else {
                    imageView.setEnabled(false);
                    params.leftMargin = DensityUtil.dip2px(8);
                }

                imageView.setLayoutParams(params);
                ll_point_group.addView(imageView);
            }


            //监听页面的改变
            viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
            tv_title.setText(topnews.get(0).getTitle());

            //准备listview对应的数据集合
            news = bean.getData().getNews();
            //设置listview的适配器
            tabDetailPagerListAdapter = new TabDetailPagerListAdapter();
            listView.setAdapter(tabDetailPagerListAdapter);
        } else {
            isLoadMore = false;
            List<TabDetailPagerBean.DataEntity.NewsData> morenews = bean.getData().getNews();
            //添加到原来集合中
            news.addAll(morenews);
            //刷新适配器
            tabDetailPagerListAdapter.notifyDataSetChanged();
        }

        //发消息每隔4s切换一次viewpager页面
        if (internalHandler == null) {
            internalHandler = new InternalHandler();
        }
        //把消息队列所有的消息和回调移除
        internalHandler.removeCallbacksAndMessages(null);
        internalHandler.postDelayed(new MyEunnable(), 4000);

    }

    class MyEunnable implements Runnable {

        @Override
        public void run() {
            internalHandler.sendEmptyMessage(0);
        }
    }

    class InternalHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //切换viewpager的下一个页面
            int item = (viewPager.getCurrentItem() + 1)%topnews.size();
            viewPager.setCurrentItem(item);
            internalHandler.postDelayed(new MyEunnable(),4000);
        }
    }


    class TabDetailPagerListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view == null) {
                view = View.inflate(context, R.layout.item_tabdetail_pager, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_icon = view.findViewById(R.id.iv_icon);
                viewHolder.tv_title = view.findViewById(R.id.tv_title);
                viewHolder.tv_time = view.findViewById(R.id.tv_time);

                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            //根据位置得到数据
            TabDetailPagerBean.DataEntity.NewsData newsData = news.get(i);
            String imgUrl = Contants.BASE_URL + newsData.getListimage();
            Log.i("yuanhetao", "imgUrl==" + imgUrl);
            x.image().bind(viewHolder.iv_icon, imgUrl, imageOptions);

            viewHolder.tv_title.setText(newsData.getTitle());

            viewHolder.tv_time.setText(newsData.getPubdate());

            String idArray = CacheUtils.getString(context, READ_ARRAY_ID);
            if (idArray.contains(newsData.getId() + "")) {
                //设置灰色
                viewHolder.tv_title.setTextColor(Color.GRAY);
            } else {
                //设置黑色
                viewHolder.tv_title.setTextColor(Color.BLACK);
            }
            return view;
        }
    }

    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_title;
        TextView tv_time;
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            //1.设置文本
            tv_title.setText(topnews.get(i).getTitle());
            //2.红点高亮
            //之前变为灰色
            ll_point_group.getChildAt(position).setEnabled(false);
            ll_point_group.getChildAt(i).setEnabled(true);
            position = i;
        }

        private boolean isDragging = false;
        @Override
        public void onPageScrollStateChanged(int i) {
            if (i == ViewPager.SCROLL_STATE_DRAGGING) { //拖拽
                isDragging = true;
                //移除
                internalHandler.removeCallbacksAndMessages(null);
            }else if (i == ViewPager.SCROLL_STATE_SETTLING) { //惯性
                isDragging = false;
                internalHandler.removeCallbacksAndMessages(null);
                internalHandler.postDelayed(new MyEunnable(),4000);

            }else if (i == ViewPager.SCROLL_STATE_IDLE) {  //静止状态
                isDragging = false;

            }
        }
    }

    class TabDetailPagerTopNewsAdapter extends PagerAdapter {

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            imageView.setBackgroundResource(R.drawable.home_scroll_default);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);
            //联网请求图片
            TabDetailPagerBean.DataEntity.TopnewsData topnewsData = topnews.get(position);
            String imgUrl = Contants.BASE_URL + topnewsData.getTopimage();
            x.image().bind(imageView, imgUrl);

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            internalHandler.removeCallbacksAndMessages(null);
                            break;
                        case MotionEvent.ACTION_UP:
                            internalHandler.removeCallbacksAndMessages(null);
                            internalHandler.postDelayed(new MyEunnable(),4000);
                            break;
//                        case MotionEvent.ACTION_CANCEL:
//                           internalHandler.removeCallbacksAndMessages(null);
//                           internalHandler.postDelayed(new MyEunnable(),4000);
//                           break;
                    }

                    return true;
                }
            });

            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }
    }

    private TabDetailPagerBean parseJson(String saveJson) {
        return new Gson().fromJson(saveJson, TabDetailPagerBean.class);

    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(url);
        params.setConnectTimeout(4000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CacheUtils.putString(context, url, result);
                LogUtil.e(childrenData.getTitle() + "-页面数据请求成功==" + result);
                ProcessData(result);

                //隐藏下拉刷新控件,重新显示数据，更新时间
                listView.onRefreshFinish(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e(childrenData.getTitle() + "-页面数据请求失败==" + ex.getMessage());
                //隐藏下拉刷新，不更新时间，只是隐藏
                listView.onRefreshFinish(false);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e(childrenData.getTitle() + "-页面数据请求取消==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e(childrenData.getTitle() + "-页面数据请求完成");
            }
        });
    }

}
