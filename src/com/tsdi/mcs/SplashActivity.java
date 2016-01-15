package com.tsdi.mcs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class SplashActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		View view = View.inflate(this, R.layout.activity_splash, null);
		setContentView(view);
		AlphaAnimation mAlphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		mAlphaAnimation.setDuration(2000);
		view.startAnimation(mAlphaAnimation);
		mAlphaAnimation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				startActivity(new Intent(SplashActivity.this, LoginActivity.class));
				finish();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
	}
}
