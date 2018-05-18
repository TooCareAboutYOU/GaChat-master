package com.gachat.main.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;


public class MyApplication extends Application {

    private static final String TAG = "SplashActivity";

    private static MyApplication sInstance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.i(TAG, "MyApplication attachBaseContext: " + System.currentTimeMillis());
//        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        ApplicationHelper.init(this);


    }

    public static synchronized MyApplication getInstance() {
        return sInstance;
    }
}