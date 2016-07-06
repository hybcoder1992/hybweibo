package org.hyb.weibo.controller;

import java.util.ArrayList;

import org.hyb.weibo.fragment.HomeFragment;
import org.hyb.weibo.fragment.MessageFragment;
import org.hyb.weibo.fragment.SearchFragment;
import org.hyb.weibo.fragment.UserFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;






/**
 * 用来控制fragment的跳转
 * */
public class FragmentController {
	//把fragment添加到那个容器(layout)
	private int containerId;
	//存放所有的fragment
	private ArrayList<Fragment> fragments;
	private FragmentManager fm;
	public static FragmentController controller;
	public static FragmentController getInstance(FragmentActivity activity, int containerId)
	{
		if(controller==null)
			controller=new FragmentController(activity,containerId);
		return controller;
	}
	private FragmentController(){};
	private FragmentController(FragmentActivity activity, int containerId) {
		this.containerId = containerId;
		fm = activity.getSupportFragmentManager();
		initFragment();
	}
	private void initFragment()
	{
		fragments = new ArrayList<Fragment>();
		fragments.add(new HomeFragment());
		fragments.add(new MessageFragment());
		fragments.add(new SearchFragment());
		fragments.add(new UserFragment());
		
		FragmentTransaction ft = fm.beginTransaction();
		for(Fragment fragment : fragments) {
			ft.add(containerId, fragment);
		}
		ft.commit();
	}
	//显示对应position的fragment
	public void showFragment(int position) {
		//先把所有的fragment隐藏
		hideFragments();
		//获取对应position的fragment
		Fragment fragment = fragments.get(position);
		FragmentTransaction ft = fm.beginTransaction();
		ft.show(fragment);
		ft.commit();
	}
	
	public void hideFragments() {
		FragmentTransaction ft = fm.beginTransaction();
		for(Fragment fragment : fragments) {
			if(fragment != null) {
				ft.hide(fragment);
			}
		}
		ft.commit();
	}
	
	public Fragment getFragment(int position) {
		return fragments.get(position);
	}
}
