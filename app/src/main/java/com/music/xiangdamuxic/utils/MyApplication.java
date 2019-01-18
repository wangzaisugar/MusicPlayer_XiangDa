package com.music.xiangdamuxic.utils;

import android.app.Application;
import android.content.Context;


public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }


    /**
     * @return Context
     */
    public static Context getContext() {
        return mContext;
    }
}
