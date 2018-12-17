package com.example.a18302.guigu_news.base;

import android.content.Context;
import android.view.View;

public abstract class MenuDetailBasePager {

    public final Context context;

    //各个详情页的页面
    public View rootView;

    public MenuDetailBasePager(Context context) {
        this.context = context;
        rootView = initView();
    }

    /**
     * 重写实现各个页面的详情
     * @return
     */
    protected abstract View initView();


    /**
     * 初始化数据
     */
    public void initData() {

    }
}
