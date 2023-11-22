package com.mgd.swordhonor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.google.firebase.FirebaseApp;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class GlobalConfiguration extends Application {

    //Will be used for retaining or checking Data Policy/User Consent

    private boolean hasUserConsent   = false;
    //Firebase Config
    public  static String GAME_URL;
    private static final   String APP_CODE = "WB998539";
    private static final  String USER_Consent = "userConsent";
    public static Boolean navStatus = true;

    @Override
    public void onCreate() {
        super.onCreate();

        //SharedPreferences <-- used to save user options/user selection
        SharedPreferences prefs = getSharedPreferences(APP_CODE, Context.MODE_PRIVATE);
        hasUserConsent = prefs.getBoolean(USER_Consent, false);

        // Initialize Facebook SDK, Adjust,AppsFlyer
        //TODO

    }

    //region[ Firebase Remote Config ]
    public static void setupRemoteConfig(Context context, Activity activity,Boolean hasFirebase)
    {
        if (Boolean.TRUE.equals(hasFirebase))
        {
            FirebaseApp.initializeApp(context);
            FirebaseRemoteConfig remoteCFG = FirebaseRemoteConfig.getInstance();
            FirebaseRemoteConfigSettings settingsCFG = new FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(3600)
                    .build();

            remoteCFG.setConfigSettingsAsync(settingsCFG);

            remoteCFG.fetchAndActivate().addOnCompleteListener(activity,task -> {
                if (task.isSuccessful())
                {
                    GAME_URL = remoteCFG.getString("apiURL") + "?appid=" + APP_CODE;
                }
            });
        }
    }

    //  [ Firebase Remote Config]

    //region [ User Consent / Data Policy ]
    public void checkUserConsent(Context context, Activity activity, Boolean hasPolicy)
    {
        setupRemoteConfig(context , activity, true);

        if(!hasUserConsent && Boolean.TRUE.equals(hasPolicy))
        {
            showConsentDialog(context, activity);
        }
    }

    private void showConsentDialog(Context context, Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_consent, null);
        WebView userConsent = dialogView.findViewById(R.id.userConsent);

        String privacyPolicyURL = "https://win11apps.site/policy";
        userConsent.setWebViewClient(new WebViewClient());
        userConsent.loadUrl(privacyPolicyURL);

        builder.setTitle("Data Privacy Policy");
        builder.setView(dialogView);

        builder.setPositiveButton("I Agree",((dialog, which) ->{
            setConsentValue(true);
            loadActivity(activity);

        } ));
        builder.setNegativeButton("Don't Agree",((dialog,which)->
        {
            activity.finishAffinity();
        } ));

        builder.show();

    }

    private void setConsentValue(boolean userChoice)
    {
        hasUserConsent = userChoice;

        //FACEBOOK,ADJUST,APPSFLYER
      //  FacebookSdk.setAutoInitEnabled(userChoice);
       // FacebookSdk.fullyInitialize();
       // setAutoLogAppEventsEnabled(userChoice);
       // setAdvertiserIDCollectionEnabled(userChoice);


        SharedPreferences prefs = getSharedPreferences(APP_CODE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(USER_Consent,userChoice);
        editor.apply();
        editor.commit();


    }

    public Boolean getUserConsent()
    {
        SharedPreferences prefs = getSharedPreferences(APP_CODE,Context.MODE_PRIVATE);
        hasUserConsent = prefs.getBoolean(USER_Consent, false);
        return hasUserConsent;
    }


    private void loadActivity(Activity activity)
    {
        Intent newActivity = new Intent(activity, AppContent.class);
        newActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(newActivity);
    }
    //EndRegion
}
