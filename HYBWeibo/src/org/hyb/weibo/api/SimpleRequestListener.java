package org.hyb.weibo.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.security.auth.PrivateCredentialPermission;

import org.hyb.weibo.utils.Logger;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import android.app.ProgressDialog;
import android.content.Context;

public class SimpleRequestListener implements RequestListener {
	private Context context;
	private ProgressDialog progressDialog;
	public SimpleRequestListener(Context context,ProgressDialog progressDialog)
	{
		this.context=context;
		this.progressDialog=progressDialog;
	}
	@Override
	public void onComplete(String response) {
		// TODO Auto-generated method stub
		onAllDone();
		//Logger.show("request onComplete", response);
	}

	@Override
	public void onComplete4binary(ByteArrayOutputStream responseOS) {
		// TODO Auto-generated method stub
		onAllDone();
		Logger.show("request onComplete4binary", responseOS.size()+"");
	}

	@Override
	public void onIOException(IOException e) {
		// TODO Auto-generated method stub
		onAllDone();
		Logger.show("request onIOException", e.toString());
	}

	@Override
	public void onError(WeiboException e) {
		// TODO Auto-generated method stub
		onAllDone();
		Logger.show("request onError", e.toString());
	}
	public void onAllDone()
	{
		if(progressDialog!=null)
			progressDialog.dismiss();
	}

}
