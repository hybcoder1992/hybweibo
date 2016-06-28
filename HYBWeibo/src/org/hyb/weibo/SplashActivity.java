package org.hyb.weibo;

import org.hyb.weibo.activity.BaseActivity;
import org.hyb.weibo.activity.LoginActivity;
import org.hyb.weibo.activity.MainActivity;
import org.hyb.weibo.constants.AccessTokenKeeper;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SplashActivity extends BaseActivity {
	Oauth2AccessToken accessToken;
	private static final int SPLASH_DUR_TIME=1000;
	private static final int WHAT_INTENT_2_MAIN=1;
	private static final int WHAT_INTENT_2_LOGIN=2;
	Handler handler=new Handler(){@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		switch (msg.what) {
		case WHAT_INTENT_2_MAIN:
			intent2Activity(MainActivity.class);
			finish();
			break;
		case WHAT_INTENT_2_LOGIN:
			intent2Activity(LoginActivity.class);
			finish();
			break;
		default:
			break;
		}
	}};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		accessToken=AccessTokenKeeper.readAccessToken(getApplicationContext());
		if(accessToken.isSessionValid())
		{
			handler.sendEmptyMessageDelayed(WHAT_INTENT_2_MAIN, SPLASH_DUR_TIME);
		}
		else {
			handler.sendEmptyMessageDelayed(WHAT_INTENT_2_LOGIN, SPLASH_DUR_TIME);
		}
	}
}
