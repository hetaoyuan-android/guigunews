package com.example.a18302.guigu_news.utils;

import android.graphics.Bitmap;
import android.os.Handler;


/**
 * 图片缓存的工具类
 */
public class BitmapCacheUtils {

    //网络缓存工具类
    private NetCacheUtils netCacheUtils;

    /**
     * 本地缓存工具类
     *
     * @param handler
     */
    private LocalCacheUtils localCacheUtils;

    /**
     * 内存缓存
     * @param handler
     */
    private MemoryCacheUtils memoryCacheUtils;


    public BitmapCacheUtils(Handler handler) {
        memoryCacheUtils = new MemoryCacheUtils();
        localCacheUtils = new LocalCacheUtils(memoryCacheUtils);
        netCacheUtils = new NetCacheUtils(handler, localCacheUtils,memoryCacheUtils);
    }


    /**
     * 三级缓存的设计步骤：
     * 从内存中去图片
     * 从本地文件中去图片
     * 向内存中保存一份
     * 请求网络图片，获取图片，显示到控件上 Handler,position
     * 内存保存一份
     * 本地存一份
     *
     * @param imageUrl
     * @param i
     * @return
     */
    public Bitmap getBitmap(String imageUrl, int position) {
        //1.内存
        if (memoryCacheUtils != null) {
            Bitmap bitmap = memoryCacheUtils.getBitmapFromUrl(imageUrl);
            if (bitmap != null) {
                LogUtil.e("内存加载图片成功==" + position);
                return bitmap;
            }
        }

        //2.本地
        if (localCacheUtils != null) {
            Bitmap bitmap = localCacheUtils.getBitmapFromUrl(imageUrl);
            if (bitmap != null) {
                LogUtil.e("本地加载图片成功==" + position);
                return bitmap;
            }
        }

        //3.网络
        netCacheUtils.getBitmapFromNet(imageUrl, position);
        return null;
    }
}
