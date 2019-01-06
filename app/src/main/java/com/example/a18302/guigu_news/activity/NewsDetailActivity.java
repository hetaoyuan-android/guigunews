package com.example.a18302.guigu_news.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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

    private WebSettings webSettings;

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
            showChangeTextSizeDialog();

            // 分享
        } else if (v == ibShare) {

        }
    }

    private int tempSize = 2;
    private int realSize = tempSize;

    /**
     * 文字大小设置的改变
     */
    private void showChangeTextSizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置文字大小");
        String[] items = {"超大字体","大字体","正常字体","小字体","超小字体"};
        builder.setSingleChoiceItems(items, realSize, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tempSize = i;
            }
        });
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                realSize = tempSize;
                changeTextSize(realSize);
            }
        });
        builder.show();
    }

    private void changeTextSize(int realSize) {
        switch (realSize) {
            case 0:
                webSettings.setTextZoom(200);
                break;
            case 1:
                webSettings.setTextZoom(150);
                break;
            case 2:
                webSettings.setTextZoom(100);
                break;
            case 3:
                webSettings.setTextZoom(75);
                break;
            case 4:
                webSettings.setTextZoom(50);
                break;
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
        webSettings = webview.getSettings();
        //设置支持javaScript
        webSettings.setJavaScriptEnabled(true);
        //设置双击变大变小
        webSettings.setUseWideViewPort(true);
        //增加缩放按钮
        webSettings.setBuiltInZoomControls(true);

        //设置文字大小
//        webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        webSettings.setTextZoom(100);
        //不让当前的网页跳转到系统的浏览器
        webview.setWebViewClient(new WebViewClient() {
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
