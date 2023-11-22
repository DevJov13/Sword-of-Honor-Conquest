package com.mgd.swordhonor;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AppContent extends AppCompatActivity {



    private boolean hasUserConsent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_content);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();

        WebView appcontent = findViewById(R.id.appContent);
        appcontent.setWebViewClient(new WebViewClient());
        appcontent.getSettings().setJavaScriptEnabled(true);
        appcontent.loadUrl(GlobalConfiguration.GAME_URL);


    }
}