package com.byl.mvpdemo.view.pullrecyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.byl.mvpdemo.R;


public class RefreshAnimView extends View{

	private Bitmap daishu;
	private int measuredWidth;
	private int measuredHeight;
	private float mCurrentProgress;
	private int mCurrentAlpha;
	private Paint mPaint;
	private Bitmap scaleDaishu;

	public RefreshAnimView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public RefreshAnimView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RefreshAnimView(Context context) {
		super(context);
		init();
	}
	private void init(){
		//袋鼠bitmap
		daishu = BitmapFactory.decodeResource(getResources(), R.mipmap.takeout_img_list_loading_pic1);
		//来个画笔，我们注意到袋鼠都有一个渐变效果的，我们用
		//mPaint.setAlpha来实现这个渐变的效果
		mPaint = new Paint();
		//首先设置为完全透明
		mPaint.setAlpha(0);
	}

	/**
	 * 测量方法
	 * @param widthMeasureSpec
	 * @param heightMeasureSpec
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
	}
	//测量宽度
	private int measureWidth(int widthMeasureSpec){
		int result = 0;
		int size = MeasureSpec.getSize(widthMeasureSpec);
		int mode = MeasureSpec.getMode(widthMeasureSpec);
		if (MeasureSpec.EXACTLY == mode) {
			result = size;
		}else {
			result = daishu.getWidth();
			if (MeasureSpec.AT_MOST == mode) {
				result = Math.min(result, size);
			}
		}
		return result;
	}
	//测量高度
	private int measureHeight(int heightMeasureSpec){
		int result = 0;
		int size = MeasureSpec.getSize(heightMeasureSpec);
		int mode = MeasureSpec.getMode(heightMeasureSpec);
		if (MeasureSpec.EXACTLY == mode) {
			result = size;
		}else {
			result = daishu.getHeight();
			if (MeasureSpec.AT_MOST == mode) {
				result = Math.min(result, size);
			}
		}
		return result;
	}
	//在这里面拿到测量后的宽和高，w就是测量后的宽，h是测量后的高
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		measuredWidth = w;
		measuredHeight = h;
		//根据测量后的宽高来对袋鼠做一个缩放
		scaleDaishu = Bitmap.createScaledBitmap(daishu,measuredWidth,measuredHeight,true);
	}

	/**
	 * 绘制方法
	 * @param canvas
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.scale(mCurrentProgress, mCurrentProgress, measuredWidth/2, measuredHeight);
		mPaint.setAlpha(mCurrentAlpha);
		canvas.drawBitmap(scaleDaishu, 0, 0, mPaint);
	}

	/**
	 * 根据进度对袋鼠进行缩放
	 * @param currentProgress
	 */
	public void setCurrentProgress(float currentProgress){
		this.mCurrentProgress = currentProgress;
		this.mCurrentAlpha = (int) (currentProgress*255);
	}
}
