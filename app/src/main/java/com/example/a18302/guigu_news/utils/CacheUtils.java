package com.example.a18302.guigu_news.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.a18302.guigu_news.SplashActivity;
import com.example.a18302.guigu_news.activity.GuideActivity;

public class CacheUtils {
    public static boolean getBoolean(Context context,String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key,false);
    }

    public static void putBoolean(Context context,String key,boolean b) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("atguigu",Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(key,b).commit();
    }
}
