package com.example.a18302.guigu_news.fragment;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a18302.guigu_news.MainActivity;
import com.example.a18302.guigu_news.R;
import com.example.a18302.guigu_news.base.BasePager;
import com.example.a18302.guigu_news.domain.NewsCenterPagerBean;
import com.example.a18302.guigu_news.utils.DensityUtil;
import com.example.a18302.guigu_news.utils.LogUtil;

import java.util.List;

public class LeftMenuFragment extends BasemenuFragment {
    private List<NewsCenterPagerBean.DataEntity> data;
    private LeftmenuFragmentAdapter adapter;
    private ListView listView;

    private int position;
    @Override
    public View initView() {
        listView = new ListView(context);
        listView.setPadding(0, DensityUtil.dip2px(context,40),0,0);
        listView.setDividerHeight(0);
        listView.setCacheColorHint(Color.TRANSPARENT);
        //设置按下的listview的item不变色
        listView.setSelector(android.R.color.transparent);

        //item的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //1.记录点击位置，变成红色
                position = i;
                adapter.notifyDataSetChanged();
                //2.把左侧菜单关闭
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.getSlidingMenu().toggle();
                //3.切换到对应的详情，
            }
        });
        return listView;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("左侧菜单的页面被初始化了");
    }

    /**
     * 接收数据
     *
     * @param data
     */
    public void setData(List<NewsCenterPagerBean.DataEntity> data) {
        this.data = data;
        for (int i = 0; i < data.size(); i++) {
            LogUtil.e("title==" + data.get(i).getTitle());
        }

        adapter = new LeftmenuFragmentAdapter();
        //设置适配器
        listView.setAdapter(adapter);
    }

    class LeftmenuFragmentAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView textView = (TextView) View.inflate(context,R.layout.item_leftmenu,null);
            textView.setText(data.get(i).getTitle());
            if (position == i) {
                textView.setEnabled(true);
            } else {
                textView.setEnabled(false);
            }
            return textView;
        }
    }
}
