package com.example.safezone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NewsWebsite extends AppCompatActivity {
    public static final String NEWS_CARD_STRING = "newsUrl";
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_website);

        Intent intent = getIntent();
        if (intent != null){
            String url = intent.getStringExtra(NEWS_CARD_STRING);
            if (url != null){
                webView = findViewById(R.id.newsWebviewId);
                webView.setWebViewClient(new WebViewClient());
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl(url);
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){

            webView.goBack();

        }else {
            super.onBackPressed();
        }
    }
}