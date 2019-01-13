package com.example.a18302.guigu_news.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 根据url获取图片
 * 本地缓存工具类
 */
public class LocalCacheUtils {
    private final MemoryCacheUtils memoryCacheUtils;

    public LocalCacheUtils(MemoryCacheUtils memoryCacheUtils) {
        this.memoryCacheUtils = memoryCacheUtils;
    }

    public Bitmap getBitmapFromUrl(String imageUrl) {
        //判断sdcard是否存在
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //保存图片/mnt/sdcard/guigunews/http://192.168.1.35:8080/xsxxx.png
            //保存图片/mnt/sdcard/guigunews/llkskkddkfsd  md5加密
            try {
                String fileName = MD5Encoder.encode(imageUrl); //llkskkddkfsd
                File file = new File(Environment.getExternalStorageDirectory() + "/guigunews",fileName);

                if (file.exists()) {
                    FileInputStream is = new FileInputStream(file);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    if (bitmap != null) {
                        memoryCacheUtils.putBitmap(imageUrl,bitmap);
                        LogUtil.e("把图片从本地保存到内存中");
                    }
                    return bitmap;
                }

            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("图片获取失败");
            }

        }
        return null;
    }

    /**
     * 根据url保存图片
     *
     * @param imageUrl
     * @param bitmap
     */
    public void putBitmap(String imageUrl, Bitmap bitmap) {
        //判断sdcard是否存在
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //保存图片/mnt/sdcard/guigunews/http://192.168.1.35:8080/xsxxx.png
            //保存图片/mnt/sdcard/guigunews/llkskkddkfsd  md5加密
            try {
                String fileName = MD5Encoder.encode(imageUrl); //llkskkddkfsd
                File file = new File(Environment.getExternalStorageDirectory() + "/guigunews",fileName);
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                if (!file.exists()) {
                    file.createNewFile();
                }
                //保存图片
                bitmap.compress(Bitmap.CompressFormat.PNG,100,new FileOutputStream(file));

            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("图片本地缓存失败");
            }

        }
    }
}
