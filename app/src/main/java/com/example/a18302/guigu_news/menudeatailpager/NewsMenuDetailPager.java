package com.example.a18302.guigu_news.menudeatailpager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.a18302.guigu_news.base.MenuDetailBasePager;
import com.example.a18302.guigu_news.pager.NewsCenterPager;
import com.example.a18302.guigu_news.utils.LogUtil;

public class NewsMenuDetailPager extends MenuDetailBasePager {

    private TextView textView;

    public NewsMenuDetailPager(Context context) {
        super(context);
    }

    @Override
    protected View initView() {
        textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("新闻详情页面数据被初始化了");
        textView.setText("新闻详情页面");
    }
}
