package com.mgd.swordhonor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Loading extends AppCompatActivity {

    private boolean hasUserConsent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_consent);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();

        ((GlobalConfiguration)getApplication()).checkUserConsent(this,this,true);


        hasUserConsent = ((GlobalConfiguration) getApplication()).getUserConsent();

        if (hasUserConsent)
        {
            new Handler().postDelayed(() -> {
                Intent appContent = new Intent(Loading.this, AppContent.class);
                appContent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(appContent);
            },1800);
        }
    }
}