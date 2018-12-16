package com.example.a18302.guigu_news.fragment;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.a18302.guigu_news.MainActivity;
import com.example.a18302.guigu_news.R;
import com.example.a18302.guigu_news.adapter.ContentFragmentAdapter;
import com.example.a18302.guigu_news.base.BasePager;
import com.example.a18302.guigu_news.pager.GovaffairPager;
import com.example.a18302.guigu_news.pager.HemoPager;
import com.example.a18302.guigu_news.pager.NewsCenterPager;
import com.example.a18302.guigu_news.pager.SettingPager;
import com.example.a18302.guigu_news.pager.SmartServicePager;
import com.example.a18302.guigu_news.utils.LogUtil;
import com.example.a18302.guigu_news.view.NoScrollViewpager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

public class ContentFragment extends BasemenuFragment {

    @ViewInject(R.id.viewpager)
    private NoScrollViewpager viewPager;

    @ViewInject(R.id.rg_main)
    private RadioGroup rg_main;
    //装5个页面集合
    private ArrayList<BasePager> basePagers;

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.content_fragment, null);
//        viewPager = view.findViewById(R.id.viewpager);
//        rg_main = view.findViewById(R.id.rg_main);
        //把视图注入到框架中
        x.view().inject(ContentFragment.this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("正文Fragment的页面被初始化了");
        //初始化五个页面，并且放到集合中
        basePagers = new ArrayList<>();
        basePagers.add(new HemoPager(context));
        basePagers.add(new NewsCenterPager(context));
        basePagers.add(new SmartServicePager(context));
        basePagers.add(new GovaffairPager(context));
        basePagers.add(new SettingPager(context));

        //设置适配器
        viewPager.setAdapter(new ContentFragmentAdapter(basePagers));

        //设置radioGroup的选中状态的改变
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_home: //主页的id
                        viewPager.setCurrentItem(0,false);
                        isEnableSlideMenu(SlidingMenu.TOUCHMODE_NONE);
                        break;
                    case R.id.rb_newscenter:
                        viewPager.setCurrentItem(1,false);
                        isEnableSlideMenu(SlidingMenu.TOUCHMODE_FULLSCREEN);
                        break;
                    case R.id.rb_smartservice:
                        viewPager.setCurrentItem(2,false);
                        isEnableSlideMenu(SlidingMenu.TOUCHMODE_NONE);
                        break;
                    case R.id.rb_govaffair:
                        viewPager.setCurrentItem(3,false);
                        isEnableSlideMenu(SlidingMenu.TOUCHMODE_NONE);
                        break;
                    case R.id.rb_setting:
                        viewPager.setCurrentItem(4,false);
                        isEnableSlideMenu(SlidingMenu.TOUCHMODE_NONE);
                        break;
                }
            }
        });



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                //调用被选中页面的initData()
                basePagers.get(i).initData();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        //默认主页面
        rg_main.check(R.id.rb_home);
        basePagers.get(0).initData();
        isEnableSlideMenu(SlidingMenu.TOUCHMODE_NONE);
    }

    private void isEnableSlideMenu(int touchmodeFullScreen) {
        MainActivity mainActivity = (MainActivity) context;
        mainActivity.getSlidingMenu().setTouchModeAbove(touchmodeFullScreen);
    }
}
