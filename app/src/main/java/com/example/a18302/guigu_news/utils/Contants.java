package com.example.a18302.guigu_news.utils;

public class Contants {

    /**
     * 联网请求的ip和端口
     */
    public static final String BASE_URL = "http://192.168.1.35:8080/web_home";
//    public static final String BASE_URL = "http://192.168.1.35:8080/web_home";

//    public static final String BASE_URL = "http://10.0.2.2:8080/web_home";

    /**
     * 新闻中心的网络地址
     */
    public static final String NEWSCENTER_PAGER_URL = BASE_URL+"/static/api/news/categories.json";

    /**
     * 商品热卖
     */
    public static final String WARES_HOT_URL = "http://192.168.1.35:8080/course_api/wares/hot?pageSize=";
}
