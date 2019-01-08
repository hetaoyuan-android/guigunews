package com.example.a18302.guigu_news.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.a18302.guigu_news.MainActivity;
import com.example.a18302.guigu_news.R;

public class BasePager {

    public final Context context;
    //视图，代表不同的页面
    public View rootView;

    //显示标题
    public TextView tv_title;

    //显示侧滑的
    public ImageButton ib_menu;

    //加载主页面
    public FrameLayout fl_content;

    public ImageButton ib_switch_list_grid;

    public BasePager(Context context) {
        this.context = context;
        rootView = initView();
    }

    /**
     * 初始化公共视图
     * @return
     */
    private View initView() {
        View view = View.inflate(context, R.layout.base_pager,null);
        tv_title = view.findViewById(R.id.tv_title);
        ib_menu = view.findViewById(R.id.ib_menu);
        fl_content = view.findViewById(R.id.fl_content);
        ib_switch_list_grid = view.findViewById(R.id.ib_switch_list_grid);
        ib_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.getSlidingMenu().toggle();
            }
        });
        return view;
    }

    /**
     * 初始化数据
     */
    public void initData() {
    }

}
