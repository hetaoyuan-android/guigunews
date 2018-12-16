package com.example.a18302.guigu_news.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.example.a18302.guigu_news.base.BasePager;
import com.example.a18302.guigu_news.utils.LogUtil;

public class HemoPager extends BasePager {

    public HemoPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("主页面数据被初始化了..");
        //1.设置标题
        tv_title.setText("主页面");
        //2.联网请求，得到数据，创建视图
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        //3.把子视图添加到BasePager的FrameLayout中
        fl_content.addView(textView,50,30);
        LogUtil.e("我是主页面");
        //4.绑定数据
        textView.setText("主页面内容");
    }
}
