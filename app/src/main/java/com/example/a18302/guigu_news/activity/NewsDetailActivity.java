package com.example.a18302.guigu_news.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a18302.guigu_news.R;

public class NewsDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageButton ibMenu;
    private ImageButton ibTextsize;
    private ImageButton ibShare;
    private ImageButton ib_back;

    private WebView webview;
    private ProgressBar pbLoading;

    String url;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2019-01-02 22:09:14 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        tvTitle = findViewById(R.id.tv_title);
        ibMenu = findViewById(R.id.ib_menu);
        ibTextsize = findViewById(R.id.ib_textsize);
        ibShare = findViewById(R.id.ib_share);
        ib_back = findViewById(R.id.ib_back);
        webview = findViewById(R.id.webview);
        pbLoading = findViewById(R.id.pb_loading);

        tvTitle.setVisibility(View.GONE);
        ibMenu.setVisibility(View.GONE);
        ib_back.setVisibility(View.VISIBLE);
        ibTextsize.setVisibility(View.VISIBLE);
        ibShare.setVisibility(View.VISIBLE);

        ibMenu.setOnClickListener(this);
        ib_back.setOnClickListener(this);
        ibTextsize.setOnClickListener(this);
        ibShare.setOnClickListener(this);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2019-01-02 22:09:14 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        //返回
        if (v == ib_back) {
            finish();
            // 文字大小
        } else if (v == ibTextsize) {
            // 分享
        } else if (v == ibShare) {

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        findViews();
        getData();
    }

    public void getData() {
        url = getIntent().getStringExtra("url");
        WebSettings webSettings = webview.getSettings();
        //设置支持javaScript
        webSettings.setJavaScriptEnabled(true);
        //设置双击变大变小
        webSettings.setUseWideViewPort(true);
        //增加缩放按钮
        webSettings.setBuiltInZoomControls(true);

        //不让当前的网页跳转到系统的浏览器
        webview.setWebViewClient(new WebViewClient(){
            //加载页面完成的时候回调
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbLoading.setVisibility(View.GONE);
            }
        });
        webview.loadUrl(url);
    }
}
