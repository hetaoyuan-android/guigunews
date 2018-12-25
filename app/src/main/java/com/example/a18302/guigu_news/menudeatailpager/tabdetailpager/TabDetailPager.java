package com.example.a18302.guigu_news.menudeatailpager.tabdetailpager;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.a18302.guigu_news.R;
import com.example.a18302.guigu_news.base.MenuDetailBasePager;
import com.example.a18302.guigu_news.domain.NewsCenterPagerBean;
import com.example.a18302.guigu_news.domain.NewsCenterPagerBean2;
import com.example.a18302.guigu_news.domain.TabDetailPagerBean;
import com.example.a18302.guigu_news.utils.CacheUtils;
import com.example.a18302.guigu_news.utils.Contants;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 页签页面
 */
public class TabDetailPager extends MenuDetailBasePager {
    private final NewsCenterPagerBean2.DetailPagerData.ChildrenData childrenData;

    private String url;

    public TabDetailPager(Context context, NewsCenterPagerBean2.DetailPagerData.ChildrenData childrenData) {
        super(context);
        this.childrenData = childrenData;
    }

    @Override
    protected View initView() {
        View view = View.inflate(context, R.layout.tabdetail_pager, null);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        url = Contants.BASE_URL + childrenData.getUrl();
        //把之前的缓存数据取出
        String saveJson = CacheUtils.getString(context,url);
        if (!TextUtils.isEmpty(saveJson)) {
            //解析和显示处理的数据
            ProcessData(saveJson);
        }
        //联网请求数据
        getDataFromNet();
    }

    private void ProcessData(String saveJson) {
        TabDetailPagerBean bean = parseJson(saveJson);
        LogUtil.e(childrenData.getTitle() + "解析成功" + bean.getData().getNews().get(0).getTitle());
    }

    private TabDetailPagerBean parseJson(String saveJson) {
        return new Gson().fromJson(saveJson,TabDetailPagerBean.class);

    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CacheUtils.putString(context,url,result);
                LogUtil.e(childrenData.getTitle() + "-页面数据请求成功==" + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e(childrenData.getTitle() + "-页面数据请求失败==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e(childrenData.getTitle() + "-页面数据请求取消==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e(childrenData.getTitle() + "-页面数据请求完成");
            }
        });
    }
}
