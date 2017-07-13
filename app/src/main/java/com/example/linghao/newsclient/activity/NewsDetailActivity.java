package com.example.linghao.newsclient.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.linghao.newsclient.R;
import com.example.linghao.newsclient.utils.CacheUtils;

public class NewsDetailActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String HUANCUN_TEXTSIZE = "huancun_textsize";
    private String url;
    private TextView tvTitle;
    private ImageButton ibBack;
    private ImageButton ibSize;
    private ImageButton ibShare;
    private WebView webView;
    private ProgressBar pbLoading;
    private WebSettings websetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        findViews();
       int textsize= CacheUtils.getInt(this,HUANCUN_TEXTSIZE);
        url = getIntent().getStringExtra("url");
        webView.loadUrl(url);
        websetting = webView.getSettings();
        //设置文字大小
        websetting.setTextZoom(textsize);
        //设置支持js
        websetting.setJavaScriptEnabled(true);
        //双击变大变小
        websetting.setUseWideViewPort(true);
        //缩放按钮
        websetting.setBuiltInZoomControls(true);
        //不让当前网页跳转到浏览器中
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbLoading.setVisibility(View.GONE);
            }
        });
    }


    private void findViews() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ibBack = (ImageButton) findViewById(R.id.ib_back);
        ibSize = (ImageButton) findViewById(R.id.ib_size);
        ibShare = (ImageButton) findViewById(R.id.ib_share);
        webView = (WebView) findViewById(R.id.webView);
        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);
        tvTitle.setVisibility(View.GONE);
        ibBack.setVisibility(View.VISIBLE);
        ibSize.setVisibility(View.VISIBLE);
        ibShare.setVisibility(View.VISIBLE);
        ibBack.setOnClickListener(this);
        ibSize.setOnClickListener(this);
        ibShare.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == ibBack) {
            finish();
        } else if (v == ibSize) {
            ChangeTextSizeDialog();
            // Handle clicks for ibSize
        } else if (v == ibShare) {
            // Handle clicks for ibShare
        }
    }

    private int tempsize = 2;
    private int realsize = tempsize;

    private void ChangeTextSizeDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("改变字体大小");
        String[] items = {"超大号字体", "大号字体", "正常", "小号", "超小号"};
        dialog.setSingleChoiceItems(items, realsize, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tempsize = which;
            }
        });
        dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                realsize = tempsize;
                changeTextSize(realsize);
            }
        });
        dialog.setNegativeButton("取消", null);
        dialog.show();
    }


    private void changeTextSize(int realsize) {
        switch (realsize) {
            case 0:
                websetting.setTextZoom(200);
                CacheUtils.putInt(this, HUANCUN_TEXTSIZE,200);
                break;
            case 1:
                websetting.setTextZoom(150);
                CacheUtils.putInt(this, HUANCUN_TEXTSIZE,150);

                break;
            case 2:
                websetting.setTextZoom(100);
                CacheUtils.putInt(this, HUANCUN_TEXTSIZE,100);
                break;
            case 3:
                websetting.setTextZoom(75);
                CacheUtils.putInt(this, HUANCUN_TEXTSIZE,75);
                break;
            case 4:
                websetting.setTextZoom(50);
                CacheUtils.putInt(this, HUANCUN_TEXTSIZE,50);
                break;
        }

    }

}
