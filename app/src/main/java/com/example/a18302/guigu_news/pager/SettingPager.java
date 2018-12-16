package com.example.a18302.guigu_news.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.example.a18302.guigu_news.base.BasePager;
import com.example.a18302.guigu_news.utils.LogUtil;

public class SettingPager extends BasePager {

    public SettingPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("设置页面数据被初始化了..");
        //标题
        tv_title.setText("设置中心");
        //联网请求得到数据
        TextView textView = new TextView(context);
       textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);
        //子试图添加到basePager的FrameLayout
        fl_content.addView(textView);
        LogUtil.e("我是设置页面");
        //绑定数据
        textView.setText("我是设置中心内容");



    }
}
