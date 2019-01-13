package com.example.a18302.guigu_news.utils;

import android.graphics.Bitmap;

import org.xutils.cache.LruCache;

public class MemoryCacheUtils {

    private LruCache<String,Bitmap> lruCache;

    public MemoryCacheUtils() {
        //最大内存
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 1024 / 8);
        lruCache = new LruCache<String,Bitmap>(maxSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return (value.getRowBytes() * value.getHeight() / 1024);
            }
        };

    }

    /**
     * 根据url从内存中获取url
     * @param imageUrl
     * @return
     */
    public Bitmap getBitmapFromUrl(String imageUrl) {
        return lruCache.get(imageUrl);
    }

    /**
     * 根据url保存图片到LruCache中
     * @param imageUrl
     * @param bitmap
     */
    public void putBitmap(String imageUrl, Bitmap bitmap) {
        lruCache.put(imageUrl,bitmap);
    }
}
