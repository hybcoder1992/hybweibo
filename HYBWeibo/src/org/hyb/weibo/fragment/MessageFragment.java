package org.hyb.weibo.fragment;

import org.hyb.weibo.R;
import org.hyb.weibo.utils.TitleBarBuilder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MessageFragment extends BaseFragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=View.inflate(activity, R.layout.fragment_message, null);
		new TitleBarBuilder(view)
		.setTitleText("消息");
		return view;
	}
}
