package com.byl.mvpdemo.util;

import android.util.Log;

public class LogUtil {

    public static final String LOG = "mvp";

    public static boolean isShowLog = false;//true:打印log，false:不打印log

    public static void e(String message) {
        if (!isShowLog) return;
        Log.e(LOG, message);
    }

    public static void i(String message) {
        if (!isShowLog) return;
        Log.i(LOG, message);
    }

    public static void v(String message) {
        if (!isShowLog) return;
        Log.v(LOG, message);
    }

}
