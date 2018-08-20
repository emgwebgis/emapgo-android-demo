package com.emapgo.android.demo;

import android.app.Application;
import android.content.Context;

import com.emapgo.mapsdk.SDKInitializer;
import com.emapgo.services.android.navigation.v5.navigation.EmgNavigation;

/**
 * Created by ben on 2018/7/2.
 */

public class MyApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        SDKInitializer.initialize(getApplicationContext(), getResources().getString(R.string.emg_token));
    }

    public static Context getAppContext() {
        return mContext;
    }
}
