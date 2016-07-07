package org.hyb.weibo.activity;



import org.hyb.weibo.api.HYBWeiboApi;
import org.hyb.weibo.application.BaseApplication;
import org.hyb.weibo.constants.CommonConstants;
import org.hyb.weibo.utils.Logger;
import org.hyb.weibo.utils.ToastUtils;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Toast;

public class BaseActivity extends Activity {
	protected String TAG;
	protected HYBWeiboApi weiboApi;
	protected BaseApplication application;
	protected SharedPreferences sp;
	protected ImageLoader imgLoader;
	protected Gson gson;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TAG = this.getClass().getSimpleName();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		application = (BaseApplication) getApplication();
		sp = getSharedPreferences(CommonConstants.SP_NAME, MODE_PRIVATE);
		imgLoader=ImageLoader.getInstance();
		weiboApi=new HYBWeiboApi(this);
		gson=new Gson();
	}
	protected void intent2Activity(Class<? extends Activity> tarActivity) {
		Intent intent = new Intent(this, tarActivity);
		startActivity(intent);
	}
	
	protected void showToast(String msg) {
		ToastUtils.showToast(this, msg, Toast.LENGTH_SHORT);
	}

	protected void showLog(String msg) {
		Logger.show(TAG, msg);
	}
}
