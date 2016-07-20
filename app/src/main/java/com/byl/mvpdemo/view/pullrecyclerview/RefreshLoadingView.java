package com.byl.mvpdemo.view.pullrecyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;

import com.byl.mvpdemo.R;


public class RefreshLoadingView extends View {

    private Bitmap endBitmap;

    public RefreshLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RefreshLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RefreshLoadingView(Context context) {
        super(context);
        init();
    }

    private void init() {
        endBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.takeout_img_list_loading_pic1);
    }

    /**
     * 只需要测量方法
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        int result = 0;
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        if (MeasureSpec.EXACTLY == mode) {
            result = size;
        } else {
            result = endBitmap.getWidth();
            if (MeasureSpec.AT_MOST == mode) {
                result = Math.min(size, result);
            }
        }
        return result;
    }

    private int measureHeight(int heightMeasureSpec) {
        int result = 0;
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        if (MeasureSpec.EXACTLY == mode) {
            result = size;
        } else {
            result = endBitmap.getHeight();
            if (MeasureSpec.AT_MOST == mode) {
                result = Math.min(size, result);
            }
        }
        return result;
    }

}
