package org.hyb.weibo.fragment;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.hyb.weibo.R;
import org.hyb.weibo.api.HYBWeiboApi;
import org.hyb.weibo.api.SimpleRequestListener;
import org.hyb.weibo.model.StatusTimeLineResponse;
import org.hyb.weibo.utils.Logger;
import org.hyb.weibo.utils.TitleBarBuilder;
import org.hyb.weibo.utils.ToastUtils;

import com.google.gson.Gson;
import com.sina.weibo.sdk.api.share.ApiUtils;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFragment extends BaseFragment{
	TextView titlebar_tv;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=View.inflate(activity,R.layout.fragment_home,null);
		new TitleBarBuilder(view)
		.setTitleText("首页")
		.setLeftImage(R.drawable.navigationbar_back_sel)
		.setLeftOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ToastUtils.showToast(activity, "点击了", Toast.LENGTH_SHORT);
			}
		});
		
		HYBWeiboApi weiboApi=new HYBWeiboApi(activity);
		weiboApi.statusesHome_timeline(1, new SimpleRequestListener(activity, null){
			@Override
			public void onComplete(String response) {
				// TODO Auto-generated method stub
				super.onComplete(response);
				StatusTimeLineResponse timeLineResponse=new Gson().fromJson(response, StatusTimeLineResponse.class);
				Logger.show("hyb", timeLineResponse.getStatuses().size()+"");
			}
		});
		return view;
	}
}
