package com.example.a18302.guigu_news.menudeatailpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a18302.guigu_news.MainActivity;
import com.example.a18302.guigu_news.R;
import com.example.a18302.guigu_news.base.MenuDetailBasePager;
import com.example.a18302.guigu_news.domain.NewsCenterPagerBean2;
import com.example.a18302.guigu_news.menudeatailpager.tabdetailpager.TopicTabDetailPager;
import com.example.a18302.guigu_news.utils.LogUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class TopicMenuDetailPager extends MenuDetailBasePager {


    @ViewInject(R.id.tablayout)
    private TabLayout tablayout;

    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;

    @ViewInject(R.id.ib_tab_next)
    private ImageButton ib_tab_next;

    //页签页面的数据集合
    private List<NewsCenterPagerBean2.DetailPagerData.ChildrenData> children;

    //页签页面的集合-页面
    private ArrayList<TopicTabDetailPager> tabDetailPagers;


    public TopicMenuDetailPager(Context context, NewsCenterPagerBean2.DetailPagerData detailPagerData) {
        super(context);
        children = detailPagerData.getChildren();
    }

    @Override
    protected View initView() {
        View view = View.inflate(context, R.layout.topicmenu_detail_pager, null);
        x.view().inject(TopicMenuDetailPager.this, view);
        ib_tab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });
        return view;
    }

    @Override
    public void initData() {
        tabDetailPagers = new ArrayList<>();
        super.initData();
        LogUtil.e("新闻详情页面数据被初始化了");
        //准备新闻详情页的数据
        for (int i = 0; i < children.size(); i++) {
            tabDetailPagers.add(new TopicTabDetailPager(context, children.get(i)));
        }
        //设置viewpager的适配器
        viewPager.setAdapter(new TopicMenuDetailPager.MyNewMenuDetailPagerAdapter());
        //viewpager和tabPagerIndiacator的关联
//        tabPageIndicator.setViewPager(viewPager);
        tablayout.setupWithViewPager(viewPager);
        //注意监听页面的变化，TabPageIndicator监听页面变化
//        tabPageIndicator.setOnPageChangeListener(new TopicMenuDetailPager.MyOnChangeListener());
            viewPager.addOnPageChangeListener(new MyOnChangeListener());
            tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);

//        for (int i = 0; i < tablayout.getTabCount(); i++) {
//            TabLayout.Tab tab = tablayout.getTabAt(i);
//            tab.setCustomView(getTabView(i));
//        }
//        viewPager.setCurrentItem(tempPositon);
    }
//    private int tempPositon = 0 ;

    public View getTabView(int position){
        View view = LayoutInflater.from(context).inflate(R.layout.tab_item, null);
        TextView tv= (TextView) view.findViewById(R.id.textView);
        tv.setText(children.get(position).getTitle());
        ImageView img = (ImageView) view.findViewById(R.id.imageView);
        img.setImageResource(R.drawable.dot_focus);
        return view;
    }

    private void isEnableSlideMenu(int touchmodeFullScreen) {
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.getSlidingMenu().setTouchModeAbove(touchmodeFullScreen);
    }

    class MyOnChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            if (i == 0) {
                //SlideMenu可以全屏滑动
                isEnableSlideMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
            } else {
                isEnableSlideMenu(SlidingMenu.TOUCHMODE_NONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    class MyNewMenuDetailPagerAdapter extends PagerAdapter {

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return children.get(position).getTitle();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            TopicTabDetailPager tabDetailPager = tabDetailPagers.get(position);
            View rootView = tabDetailPager.rootView;
            tabDetailPager.initData(); //初始化数据
            container.addView(rootView);
            return rootView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return tabDetailPagers.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }
    }
}
