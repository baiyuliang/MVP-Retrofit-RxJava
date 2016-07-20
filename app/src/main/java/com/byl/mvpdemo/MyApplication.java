package com.byl.mvpdemo;

import android.app.Application;

import com.byl.mvpdemo.util.LogUtil;

/**
 * Created by baiyuliang on 2016-7-14.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.isShowLog = true;
    }
}
