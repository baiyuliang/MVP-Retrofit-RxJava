package com.byl.mvpdemo.util;

import android.content.Context;

/**
 * Created by baiyuliang on 2016-7-19.
 */
public class SysUtil {

    /**
     * 将dp转px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
