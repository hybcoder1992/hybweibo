package org.hyb.weibo.fragment;


import java.util.ArrayList;
import java.util.List;

import org.hyb.weibo.R;
import org.hyb.weibo.adapter.StatusAdapter;
import org.hyb.weibo.api.HYBWeiboApi;
import org.hyb.weibo.api.SimpleRequestListener;
import org.hyb.weibo.model.Status;
import org.hyb.weibo.model.StatusTimeLineResponse;
import org.hyb.weibo.utils.TitleBarBuilder;
import org.hyb.weibo.utils.ToastUtils;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFragment extends BaseFragment{
	TextView titlebar_tv;
	PullToRefreshListView lv_home;
	View footView;
	View view;
	int curPage=1;
	StatusAdapter adapter;
	List<Status> statuses=new ArrayList<Status>();//先创建个status的集合,把空的数据装到adapter中
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initView();
		initData(1);
		return view;
	}
	private void addData(StatusTimeLineResponse model)
	{
		for(Status status:model.getStatuses())
		{
			if(!statuses.contains(status))
				statuses.add(status);
		}
		//通知适配器更新数据
		adapter.notifyDataSetChanged();
		ListView lView=lv_home.getRefreshableView();
		if(curPage<model.getTotal_number())//如果当前页是最后一页
		{
			//添加footview
			
			if(lView.getFooterViewsCount()==1)
				lView.addFooterView(footView);
		}else {
			//移除footview
			if(lView.getFooterViewsCount()>1)
				lView.removeFooterView(footView);
		}
	}
	
	private void initData(final int page)
	{
		HYBWeiboApi weiboApi=new HYBWeiboApi(activity);
		weiboApi.statusesHome_timeline(page, new SimpleRequestListener(activity, null){
			@Override
			public void onComplete(String response) {
				// TODO Auto-generated method stub
				super.onComplete(response);
				if(page==1)
					statuses.clear();
				curPage=page;
				//将服务器返回的json数据转成模型类
				StatusTimeLineResponse timeLineResponse=new Gson().fromJson(response, StatusTimeLineResponse.class);
				addData(timeLineResponse);
				//lv_home.setAdapter(new StatusAdapter(activity, timeLineResponse.getStatuses()));
			}
			@Override
			public void onAllDone() {
				// TODO Auto-generated method stub
				super.onAllDone();
				//告诉下拉刷新控件完成刷新
				lv_home.onRefreshComplete();
			}
		});
	}
	private void initView()
	{
		view=View.inflate(activity,R.layout.fragment_home,null);
		//设置标题栏
		new TitleBarBuilder(view)
		.setTitleText("首页")//设置中间标题文字
		.setLeftImage(R.drawable.navigationbar_back_sel)//设置左侧图片按钮
		.setLeftOnClickListener(new OnClickListener() {//给左侧按钮添加点击事件
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ToastUtils.showToast(activity, "点击了", Toast.LENGTH_SHORT);
			}
		});
		
		lv_home=(PullToRefreshListView)view.findViewById(R.id.lv_home);
		adapter=new StatusAdapter(activity, statuses);//此时适配器中的数据是空的
		lv_home.setAdapter(adapter);
		//下拉刷新
		lv_home.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				initData(1);
			}
			
		});
		//当滚动到最后一个item时加载下一页数据
		lv_home.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				initData(curPage+1);
			}
		});
		footView=(View)View.inflate(activity, R.layout.footview_loading, null);
	}
}
