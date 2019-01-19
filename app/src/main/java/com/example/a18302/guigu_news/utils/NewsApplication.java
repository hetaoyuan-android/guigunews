package com.example.a18302.guigu_news.utils;

import android.app.Application;

import com.example.a18302.guigu_news.volley.VolleyManager;

import org.xutils.BuildConfig;
import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

public class NewsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
        //初始化Volley
        VolleyManager.init(this);

        //初始化极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
