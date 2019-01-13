package com.example.a18302.guigu_news.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NetCacheUtils {
    //内存缓存
    private MemoryCacheUtils memoryCacheUtils;
    //本地缓存工具类
    private LocalCacheUtils localCacheUtils;
    private Handler handler;
    //请求图片成功
    public static int SUCCESS = 1;
    //失败
    public static int FAIL = 2;

    /**
     * 线程池服务类
     * @param handler
     */
    private ExecutorService service;

    public NetCacheUtils(Handler handler, LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils) {
        this.handler = handler;
        service = Executors.newFixedThreadPool(10);
        this.localCacheUtils = localCacheUtils;
        this.memoryCacheUtils = memoryCacheUtils;
    }

    /**
     * 联网请求，得到图片
     * @param imageUrl
     * @param position
     */
    public void getBitmapFromNet(String imageUrl, int position) {
//        new Thread(new MyRunnable(imageUrl,position)).start();

        service.execute(new MyRunnable(imageUrl,position));
    }

    class MyRunnable implements Runnable {
        private int position;
        private String imageUrl;

        public MyRunnable(String imageUrl,int position) {
            this.imageUrl = imageUrl;
            this.position = position;
        }

        @Override
        public void run() {
            //子线程
            //请求网络图片
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET"); //只能大写
                connection.setConnectTimeout(4000);
                connection.setReadTimeout(4000);
                connection.connect(); //可写可不写
                int code = connection.getResponseCode();
                if (code == 200) {
                    InputStream is = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    //显示,发消息，把bitmap发出去
                    Message msg = Message.obtain();
                    msg.what = SUCCESS;
                    msg.arg1 = position;
                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                    //内存缓存一份
                    memoryCacheUtils.putBitmap(imageUrl,bitmap);
                    //本地缓存一份
                    localCacheUtils.putBitmap(imageUrl,bitmap);
                }

            } catch (Exception e) {
                e.printStackTrace();
                Message msg = Message.obtain();
                msg.what = FAIL;
                msg.arg1 = position;
                handler.sendMessage(msg);
            }
        }
    }
}
