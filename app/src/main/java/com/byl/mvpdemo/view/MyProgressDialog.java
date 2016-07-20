package com.byl.mvpdemo.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.byl.mvpdemo.R;


/**
 * 
 * Date: 2014-06-19 <br>
 * 
 * @author byl
 */
public class MyProgressDialog extends Dialog {
	
	private ImageView iv_route;
	private TextView tv;
	private RotateAnimation mAnim;
	private boolean cancelable = true;


	public MyProgressDialog(Context context) {
		super(context, R.style.Dialog_bocop);
		init();
	}

	@SuppressWarnings("ResourceType")
	private void init() {
		View contentView = View.inflate(getContext(), R.layout.loading_dialog, null);
		 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    
			   Window win = getWindow();    
		       WindowManager.LayoutParams winParams = win.getAttributes();    
		       winParams.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;    
		       win.setAttributes(winParams);    
	       }  
		setContentView(contentView);
		
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cancelable) {
					dismiss();
				}
			}
		});
		iv_route = (ImageView) findViewById(R.id.iv_route);
		tv = (TextView) findViewById(R.id.tv);
		initAnim();
		getWindow().setWindowAnimations(R.anim.alpha_in);
	}
	
	
	private void initAnim() {
		mAnim=new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mAnim.setDuration(1500);
		mAnim.setInterpolator(new LinearInterpolator());
		mAnim.setFillAfter(true);
		mAnim.setRepeatCount(-1);
	}

	@Override
	public void show() {
		iv_route.startAnimation(mAnim);
		super.show();
	}
	
	@SuppressLint("NewApi")
	@Override
	public void dismiss() {
		mAnim.cancel();
		super.dismiss();
	}
	
	
	@Override
	public void setCancelable(boolean flag) {
		cancelable = flag;
		super.setCancelable(flag);
	}
	
	@Override
	public void setTitle(CharSequence title) {
		tv.setText(title);
	}
	
	public void setMessage(String title) {
		tv.setText(title);
	}
	
	@Override
	public void setTitle(int titleId) {
		setTitle(getContext().getString(titleId));
	}
}
