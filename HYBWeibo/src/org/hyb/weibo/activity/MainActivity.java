package org.hyb.weibo.activity;

import org.hyb.weibo.R;
import org.hyb.weibo.R.layout;
import org.hyb.weibo.controller.FragmentController;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends FragmentActivity implements OnCheckedChangeListener,OnClickListener{
	FragmentController fragmentController;
	RadioGroup rg_tab;
	ImageView iv_add;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fragmentController=FragmentController.getInstance(this, R.id.fl_content);
		fragmentController.showFragment(0);
		initView();
	}
	private void initView()
	{
		rg_tab=(RadioGroup)findViewById(R.id.rg_tab);
		iv_add=(ImageView)findViewById(R.id.iv_add);
		//给radiogroup添加OnCheckedChangeListener
		rg_tab.setOnCheckedChangeListener(this);
		iv_add.setOnClickListener(this);
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.rb_home:
			fragmentController.showFragment(0);
			break;
		case R.id.rb_message:
			fragmentController.showFragment(1);	
					break;
		case R.id.rb_search:
			fragmentController.showFragment(2);
			break;
		case R.id.rb_user:
			fragmentController.showFragment(3);
			break;
		case R.id.iv_add:
			
			break;
		default:
			break;
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
}
