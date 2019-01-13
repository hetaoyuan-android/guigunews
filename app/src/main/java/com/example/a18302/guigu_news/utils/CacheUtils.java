package com.example.a18302.guigu_news.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.example.a18302.guigu_news.SplashActivity;
import com.example.a18302.guigu_news.activity.GuideActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CacheUtils {
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public static void putBoolean(Context context, String key, boolean b) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(key, b).commit();
    }

    /**
     * 缓存文本的数据
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context, String key, String value) {
        //判断sdcard是否存在
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //mnt/sdcard/guigunews/http://192.168.1.35:8080/xsxxx.png
            //mnt/sdcard/guigunews/llkskkddkfsd  md5加密
            try {
                String fileName = MD5Encoder.encode(key); //llkskkddkfsd
                File file = new File(Environment.getExternalStorageDirectory() + "/guigunews/files", fileName);
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                if (!file.exists()) {
                    file.createNewFile();
                }
                //保存文本数据
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(value.getBytes());
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("文本数据緩存失败");
            }
        } else {
            SharedPreferences sharedPreferences = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
            sharedPreferences.edit().putString(key, value).commit();
        }
    }

    /**
     * 读取缓存的文本
     *
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        String result = "";
        FileInputStream inputStream = null;
        ByteArrayOutputStream stream = null;
        //判断sdcard是否存在
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //mnt/sdcard/guigunews/guigunews/files/xsxxx.png
            //mnt/sdcard/guigunews/files/llkskkddkfsd  md5加密
            try {
                String fileName = MD5Encoder.encode(key); //llkskkddkfsd
                File file = new File(Environment.getExternalStorageDirectory() + "/guigunews/files", fileName);
                if (file.exists()) {
                    inputStream = new FileInputStream(file);
                    stream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) != -1) {
                        stream.write(buffer, 0, length);
                    }
                    result = stream.toString();
                }
                inputStream.close();
                stream.close();

            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("文本数据緩存失败");
            }
        } else {
            SharedPreferences sharedPreferences = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
            result = sharedPreferences.getString(key, "");
        }
        return result;
    }
}
