package com.example.a18302.guigu_news;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.a18302.guigu_news.fragment.ContentFragment;
import com.example.a18302.guigu_news.fragment.LeftMenuFragment;
import com.example.a18302.guigu_news.utils.DensityUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

    public static final String  MAIN_CPNTENT_TAG = "mian_content_tag";
    public static final String LEFTMENU_TAG = "leftmenu_tag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置主界面
        setContentView(R.layout.activity_main);

        //设置左侧菜单
        setBehindContentView(R.layout.activity_leftmenu);

        //设置右侧菜单
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setSecondaryMenu(R.layout.activity_rightmenu);

        //设置显示的模式，左侧菜单+主页，左侧+主页+右侧，主页+右侧
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);

        //设置滑动的模式，边缘滑动，全屏滑动，不可以滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        //设置主页占据的宽度
        slidingMenu.setBehindOffset(DensityUtil.dip2px(this,200));

        //初始化Fragment
        initFragmen();

    }

    private void initFragmen() {
        //1.得到Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        //2.开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //3.替换
        transaction.replace(R.id.fl_main_content,new ContentFragment(),MAIN_CPNTENT_TAG);
        transaction.replace(R.id.fl_left_manu,new LeftMenuFragment(), LEFTMENU_TAG);
        //4.提交
        transaction.commit();

    }
}
