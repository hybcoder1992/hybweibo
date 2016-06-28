package org.hyb.weibo.utils;

import org.hyb.weibo.R;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class TitleBarBuilder {
	private View viewTitle;//最外面的RelativeLayout
	private TextView tvTitle;
	private TextView tvLeft;
	private TextView tvRight;
	private ImageView ivLeft;
	private ImageView ivRight ;
	public TitleBarBuilder(Activity context)
	{
		viewTitle=(View)context.findViewById(R.id.rl_titlebar);
		tvTitle=(TextView)viewTitle.findViewById(R.id.titlebar_tv);
		tvLeft=(TextView)viewTitle.findViewById(R.id.titlebar_tv_left);
		tvRight=(TextView)viewTitle.findViewById(R.id.titlebar_tv_right);
		ivLeft=(ImageView)viewTitle.findViewById(R.id.titlebar_iv_left);
		ivRight=(ImageView)viewTitle.findViewById(R.id.titlebar_iv_right);
	}
	public TitleBarBuilder(View context)
	{
		viewTitle=(View)context.findViewById(R.id.rl_titlebar);
		tvTitle=(TextView)viewTitle.findViewById(R.id.titlebar_tv);
		tvLeft=(TextView)viewTitle.findViewById(R.id.titlebar_tv_left);
		tvRight=(TextView)viewTitle.findViewById(R.id.titlebar_tv_right);
		ivLeft=(ImageView)viewTitle.findViewById(R.id.titlebar_iv_left);
		ivRight=(ImageView)viewTitle.findViewById(R.id.titlebar_iv_right);
	}
	// title
		//设置标题栏的背景
		public TitleBarBuilder setTitleBgRes(int resid) {
			viewTitle.setBackgroundResource(resid);
			return this;
		}
//设置标题栏中间的文字
		public TitleBarBuilder setTitleText(String text) {
			tvTitle.setVisibility(TextUtils.isEmpty(text) ? View.GONE
					: View.VISIBLE);
			tvTitle.setText(text);
			return this;
		}

		// left
//设置标题栏左边imageview的图片
		public TitleBarBuilder setLeftImage(int resId) {
			ivLeft.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
			ivLeft.setImageResource(resId);
			return this;
		}
//设置标题栏左边的文字
		public TitleBarBuilder setLeftText(String text) {
			tvLeft.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
			tvLeft.setText(text);
			return this;
		}
//给标题栏左边的文字或图片添加点击事件
		public TitleBarBuilder setLeftOnClickListener(OnClickListener listener) {
			if (ivLeft.getVisibility() == View.VISIBLE) {
				ivLeft.setOnClickListener(listener);
			} else if (tvLeft.getVisibility() == View.VISIBLE) {
				tvLeft.setOnClickListener(listener);
			}
			return this;
		}

		// right
//设置标题栏右边imageview的图片
		public TitleBarBuilder setRightImage(int resId) {
			ivRight.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
			ivRight.setImageResource(resId);
			return this;
		}
//设置标题栏右边的文字
		public TitleBarBuilder setRightText(String text) {
			tvRight.setVisibility(TextUtils.isEmpty(text) ? View.GONE
					: View.VISIBLE);
			tvRight.setText(text);
			return this;
		}
//给标题栏右边的文字或图片添加点击事件
		public TitleBarBuilder setRightOnClickListener(OnClickListener listener) {
			if (ivRight.getVisibility() == View.VISIBLE) {
				ivRight.setOnClickListener(listener);
			} else if (tvRight.getVisibility() == View.VISIBLE) {
				tvRight.setOnClickListener(listener);
			}
			return this;
		}

		public View build() {
			return viewTitle;
		}
}
