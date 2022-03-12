package com.example.readit;

import android.app.Application;
import android.content.Context;
public class Test extends Application {
    //private static MyApp instance;
    private static Context mContext;

    public static Context getContext() {
        //  return instance.getApplicationContext();
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //  instance = this;
        mContext = getApplicationContext();
    }
}