package com.example.a18302.guigu_news.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.example.a18302.guigu_news.R;
import com.example.a18302.guigu_news.utils.LogUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class ContentFragment extends BasemenuFragment {

    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;

    @ViewInject(R.id.rg_main)
    private RadioGroup rg_main;

    @Override
    public View initView() {
        View view = View.inflate(context,R.layout.content_fragment,null);
//        viewPager = view.findViewById(R.id.viewpager);
//        rg_main = view.findViewById(R.id.rg_main);
        //把视图注入到框架中
        x.view().inject(ContentFragment.this,view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("正文Fragment的页面被初始化了");
        rg_main.check(R.id.rb_home);
    }
}
