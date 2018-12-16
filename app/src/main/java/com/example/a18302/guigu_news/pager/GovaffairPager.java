package com.example.a18302.guigu_news.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.example.a18302.guigu_news.base.BasePager;

public class GovaffairPager extends BasePager {

    public GovaffairPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        //标题
        tv_title.setText("政要指南");
        //联网请求得到数据
        TextView textView = new TextView(context);
       textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        //子试图添加到basePager的FrameLayout
        fl_content.addView(textView);
        //绑定数据
        textView.setText("我是政要指南内容");



    }
}
