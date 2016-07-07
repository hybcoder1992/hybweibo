package org.hyb.weibo.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hyb.weibo.R;
import org.hyb.weibo.adapter.StatusCommentAdapter;
import org.hyb.weibo.adapter.StatusGridViewAdapter;
import org.hyb.weibo.api.SimpleRequestListener;
import org.hyb.weibo.model.Comment;
import org.hyb.weibo.model.CommentsResponse;
import org.hyb.weibo.model.PicUrls;
import org.hyb.weibo.model.Status;
import org.hyb.weibo.model.User;
import org.hyb.weibo.utils.DateUtils;
import org.hyb.weibo.utils.StringUtils;
import org.hyb.weibo.utils.TitleBarBuilder;
import org.hyb.weibo.widget.WrapHeightGridView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class StatusDetailActivity extends BaseActivity implements OnCheckedChangeListener,OnClickListener{
		// listView headerView - 微博信息
		private View status_detail_info;
		private ImageView iv_avatar;
		private TextView tv_subhead;
		private TextView tv_caption;
		private FrameLayout include_status_image;
		private WrapHeightGridView gv_images;
		private ImageView iv_image;
		private TextView tv_content;
		private View include_retweeted_status;
		private TextView tv_retweeted_content;
		private FrameLayout fl_retweeted_imageview;
		private GridView gv_retweeted_images;
		private ImageView iv_retweeted_image;
	
		// shadow_tab - 顶部悬浮的菜单栏
		private View shadow_status_detail_tab;
		private RadioGroup shadow_rg_status_detail;
		private RadioButton shadow_rb_retweets;
		private RadioButton shadow_rb_comments;
		private RadioButton shadow_rb_likes;
		// listView headerView - 添加至列表中作为header的菜单栏
		private View status_detail_tab;
		private RadioGroup rg_status_detail;
		private RadioButton rb_retweets;
		private RadioButton rb_comments;
		private RadioButton rb_likes;
		
		//listview--下拉刷新控件
		PullToRefreshListView plv_status_detail;
		//footerview--加载更多
		View footView;
		StatusCommentAdapter adapter;
		List<Comment> comments=new ArrayList<Comment>();
		
		//bottom controller-底部互动栏,包括转发/评论/点赞
		private LinearLayout ll_bottom_controller;
		private LinearLayout ll_share_bottom;
		private TextView tv_share_bottom;
		private LinearLayout ll_comment_bottom;
		private TextView tv_comment_bottom;
		private LinearLayout ll_like_bottom;
		private TextView tv_like_bottom;
		//评论当前已加载页数
		private long curPage=1;
	
		private Status status;//详情页的微博信息
		//是否需要滚动到评论部分
		private boolean scroll2Comment;
		
		public static final int REQUEST_WRITE_COMMENT=1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_status_detail);
		//获取intent传入的数据
		status=(Status)getIntent().getSerializableExtra("status");
		scroll2Comment=getIntent().getBooleanExtra("scroll2Comment", false);
		initView();
		initData();
		//开始加载第一页评论数据
		addFooterView(plv_status_detail, footView);
		loadComments(1);
	}
	
	private void initView()
	{
		initTitle();
		//微博信息
		initDetailHead();
		initTab();
		initListView();
		initControlBar();
	}
	private void initTitle()
	{
		new TitleBarBuilder(this)
		.setTitleText("微博正文")
		.setLeftImage(R.drawable.navigationbar_back_sel)
		.setLeftOnClickListener(this);
	}
	//初始化listView的headerview
	private void initDetailHead() {
		status_detail_info = View.inflate(this, R.layout.item_status, null);
		status_detail_info.setBackgroundResource(R.color.white);
		status_detail_info.findViewById(R.id.ll_bottom_control).setVisibility(View.GONE);
		iv_avatar = (ImageView) status_detail_info.findViewById(R.id.iv_avatar);
		tv_subhead = (TextView) status_detail_info.findViewById(R.id.tv_subhead);
		tv_caption = (TextView) status_detail_info.findViewById(R.id.tv_caption);
		include_status_image = (FrameLayout) status_detail_info.findViewById(R.id.include_status_image);
		gv_images = (WrapHeightGridView) status_detail_info.findViewById(R.id.gv_images);
		iv_image = (ImageView) status_detail_info.findViewById(R.id.iv_image);
		tv_content = (TextView) status_detail_info.findViewById(R.id.tv_content);
		include_retweeted_status = status_detail_info.findViewById(R.id.include_retweeted_status);
		tv_retweeted_content = (TextView) status_detail_info.findViewById(R.id.tv_retweeted_content);
		fl_retweeted_imageview = (FrameLayout) include_retweeted_status.findViewById(R.id.include_status_image);
		gv_retweeted_images = (GridView) fl_retweeted_imageview.findViewById(R.id.gv_images);
		iv_retweeted_image = (ImageView) fl_retweeted_imageview.findViewById(R.id.iv_image);
		iv_image.setOnClickListener(this);
			
	}
	//初始化底部互动栏
	private void initControlBar() {
		ll_bottom_controller = (LinearLayout) findViewById(R.id.status_detail_controller);
		ll_share_bottom = (LinearLayout) ll_bottom_controller.findViewById(R.id.ll_share_bottom);
		tv_share_bottom = (TextView) ll_bottom_controller.findViewById(R.id.tv_share_bottom);
		ll_comment_bottom = (LinearLayout) ll_bottom_controller.findViewById(R.id.ll_comment_bottom);
		tv_comment_bottom = (TextView) ll_bottom_controller.findViewById(R.id.tv_comment_bottom);
		ll_like_bottom = (LinearLayout) ll_bottom_controller.findViewById(R.id.ll_like_bottom);
		tv_like_bottom = (TextView) ll_bottom_controller.findViewById(R.id.tv_like_bottom);
		ll_bottom_controller.setBackgroundResource(R.color.white);
		ll_share_bottom.setOnClickListener(this);
		ll_comment_bottom.setOnClickListener(this);
		ll_like_bottom.setOnClickListener(this);
	}
	//初始化菜单栏
	private void initTab()
	{
		// shadow悬浮栏
		shadow_status_detail_tab = findViewById(R.id.status_detail_tab);//一开始是隐藏的
		shadow_rg_status_detail = (RadioGroup) shadow_status_detail_tab
				.findViewById(R.id.rg_status_detail);
		shadow_rb_retweets = (RadioButton) shadow_status_detail_tab
				.findViewById(R.id.rb_retweets);
		shadow_rb_comments = (RadioButton) shadow_status_detail_tab
				.findViewById(R.id.rb_comments);
		shadow_rb_likes = (RadioButton) shadow_status_detail_tab
				.findViewById(R.id.rb_likes);
		shadow_rg_status_detail.setOnCheckedChangeListener(this);
		// header
		status_detail_tab = View.inflate(this, R.layout.status_detail_tab, null);
		rg_status_detail = (RadioGroup) status_detail_tab
				.findViewById(R.id.rg_status_detail);
		rb_retweets = (RadioButton) status_detail_tab
				.findViewById(R.id.rb_retweets);
		rb_comments = (RadioButton) status_detail_tab
				.findViewById(R.id.rb_comments);
		rb_likes = (RadioButton) status_detail_tab
				.findViewById(R.id.rb_likes);
		rg_status_detail.setOnCheckedChangeListener(this);
	}
	private void initListView(){
		//下拉刷新控件
		plv_status_detail=(PullToRefreshListView)findViewById(R.id.plv_status_detail);
		adapter=new StatusCommentAdapter(this, comments);
		plv_status_detail.setAdapter(adapter);
		
		//footerview--加载更多
		footView=View.inflate(this, R.layout.footview_loading, null);
		
		final ListView lv=plv_status_detail.getRefreshableView();
		//微博正文的view
		lv.addHeaderView(status_detail_info);
		lv.addHeaderView(status_detail_tab);
		//下拉刷新监听
		plv_status_detail.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				loadComments(1);
			}
		});
		//滑动到底部最后一个item监听
		plv_status_detail.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				// TODO Auto-generated method stub
				//if(comments>=)
				if(comments.size()<50)
					return;
				loadComments(curPage+1);
			}
		});
		//滚动状态监听
		plv_status_detail.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				//0-pullhead 1-detailhead 2-tab
				shadow_status_detail_tab.setVisibility(firstVisibleItem>=2?View.VISIBLE:View.GONE);
			}
		});
	}
	private void initData()
	{
		//设置listview的headerview数据--微博数据
		User user=status.getUser();
		imgLoader.displayImage(user.getProfile_image_url(), iv_avatar);
		tv_subhead.setText(user.getName());
		tv_caption.setText(DateUtils.getShortTime(status.getCreated_at()+" 来自"+Html.fromHtml(status.getSource())));
		setImages(status, include_status_image, gv_images, iv_image);
		if(TextUtils.isEmpty(status.getText())){
			tv_content.setVisibility(View.GONE);
		}else {
			tv_content.setVisibility(View.VISIBLE);
			SpannableString content=StringUtils.getWeiboContent(this, tv_content, status.getText());
			tv_content.setText(content);
		}
		Status retweetedStatus = status.getRetweeted_status();
		if (retweetedStatus != null) {
			include_retweeted_status.setVisibility(View.VISIBLE);
			String retweetContent = "@" + retweetedStatus.getUser().getName()
					+ ":" + retweetedStatus.getText();
			SpannableString weiboContent = StringUtils.getWeiboContent(
					this, tv_retweeted_content, retweetContent);
			tv_retweeted_content.setText(weiboContent);
			setImages(retweetedStatus, fl_retweeted_imageview,
					gv_retweeted_images, iv_retweeted_image);
		} else {
			include_retweeted_status.setVisibility(View.GONE);
		}
		// shadow_tab - 顶部悬浮的菜单栏
		shadow_rb_retweets.setText("转发 " + status.getReposts_count());
		shadow_rb_comments.setText("评论 " + status.getComments_count());
		shadow_rb_likes.setText("赞 " + status.getAttitudes_count());
		// listView headerView - 添加至列表中作为header的菜单栏
		rb_retweets.setText("转发 " + status.getReposts_count());
		rb_comments.setText("评论 " + status.getComments_count());
		rb_likes.setText("赞 " + status.getAttitudes_count());
		
		// bottom_control - 底部互动栏,包括转发/评论/点赞
		tv_share_bottom.setText(status.getReposts_count() == 0 ?
				"转发" : status.getReposts_count() + "");
		tv_comment_bottom.setText(status.getComments_count() == 0 ?
				"评论" : status.getComments_count() + "");
		tv_like_bottom.setText(status.getAttitudes_count() == 0 ?
				"赞" : status.getAttitudes_count() + "");
	}
	private void setImages(final Status status,ViewGroup vgContainer,GridView gvImgs,final ImageView ivImg){
		if(status==null)return;
		ArrayList<PicUrls> picUrls=status.getPic_urls();
		String picUrl=status.getBmiddle_pic();
		if(picUrls!=null && picUrls.size()==1){
			vgContainer.setVisibility(View.VISIBLE);
			gvImgs.setVisibility(View.GONE);
			ivImg.setVisibility(View.VISIBLE);
			imgLoader.displayImage(picUrl, ivImg);
		}else if (picUrls!=null && picUrls.size()>1) {
			vgContainer.setVisibility(View.VISIBLE);
			ivImg.setVisibility(View.GONE);
			gvImgs.setVisibility(View.VISIBLE);
			StatusGridViewAdapter adapter=new StatusGridViewAdapter(this, picUrls);
			gvImgs.setAdapter(adapter);
		}else {
			vgContainer.setVisibility(View.GONE);
		}
	}
	/**
	 * 根据微博id返回某条微博的评论列表
	 * */
	private void loadComments(final long requestPage)
	{
		showLog("load more");
		showLog(requestPage+"");
		weiboApi.commentsShow(status.getId(), requestPage, new SimpleRequestListener(this, null){
			@Override
			public void onComplete(String response) {
				// TODO Auto-generated method stub
				super.onComplete(response);
				
				//如果是加载第一页(第一次进入或者下拉刷新)时,先清空已有数据
				if(requestPage==1){
					comments.clear();
				}
				curPage=requestPage;
				//解析返回数据string->model
				CommentsResponse commentsResponse=gson.fromJson(response, CommentsResponse.class);
				//跟新评论数信息
				
				tv_comment_bottom.setText(commentsResponse.getTotal_number()==0?"评论":commentsResponse.getTotal_number()+"");
				shadow_rb_comments.setText("评论 "+commentsResponse.getTotal_number());
				rb_comments.setText("评论 "+commentsResponse.getTotal_number());
				//将获取的评论信息添加到列表上
				addData(commentsResponse);
				//判断是否需要滚动至评论部分
				if(scroll2Comment)
				{
					plv_status_detail.getRefreshableView().setSelection(2);
					scroll2Comment=false;
				}
			}
			@Override
			public void onIOException(IOException e) {
				// TODO Auto-generated method stub
				super.onIOException(e);
				showLog("io异常");
				//通知下拉刷新控件完成刷新
				plv_status_detail.onRefreshComplete();
			}
			@Override
			public void onError(WeiboException e) {
				// TODO Auto-generated method stub
				super.onError(e);
				showLog("onError异常");
				//通知下拉刷新控件完成刷新
				plv_status_detail.onRefreshComplete();
			}
			@Override
			public void onAllDone() {
				// TODO Auto-generated method stub
				super.onAllDone();
				showLog("alldone了");
				//通知下拉刷新控件完成刷新
				plv_status_detail.onRefreshComplete();
				
			}
		});
	}
	private void addData(CommentsResponse response)
	{
		for(Comment comment:response.getComments())
		{
			if(!comments.contains(comment))
				comments.add(comment);
		}
		//添加完成后,通知listview刷新页面
		adapter.notifyDataSetChanged();
		//判断当前评论数是否达到总评论数,未达到则添加加载更多的footerview,反之移除
		if(comments.size()<response.getTotal_number())
		{
			addFooterView(plv_status_detail, this.footView);
		}
		else{
			removeFooterView(plv_status_detail, this.footView);
		}
	}
	 private void addFooterView(PullToRefreshListView plv,View footView) {
		// TODO Auto-generated method stub
		 ListView lv=plv.getRefreshableView();
			if(lv.getFooterViewsCount()==1)
				lv.addFooterView(footView);
	}
	 private void removeFooterView(PullToRefreshListView plv,View footView) {
		// TODO Auto-generated method stub
		 ListView lv=plv.getRefreshableView();
			if(lv.getFooterViewsCount()>1)
				lv.removeFooterView(footView);
	}
	 @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 // TODO Auto-generated method stub
		 super.onActivityResult(requestCode, resultCode, data);
		 //如果back键返回,取消发评论,则直接return,不做处理
		 if(resultCode==RESULT_CANCELED){
			 return;
		 }
		 switch (requestCode) {
		case REQUEST_WRITE_COMMENT:
			boolean sendCommentSuccess=data.getBooleanExtra("sendCommentSuccess", false);
			if(sendCommentSuccess){
				scroll2Comment=true;
				loadComments(1);
			}
			break;

		default:
			break;
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.titlebar_iv_left:
			StatusDetailActivity.this.finish();
			break;
		case R.id.ll_comment_bottom:
			//跳转到写评论界面
			Intent intent=new Intent(this,WriteCommentActivity.class);
			intent.putExtra("status", status);
			startActivityForResult(intent, REQUEST_WRITE_COMMENT);
			break;
		default:
			break;
		}
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		// 更新tab菜单栏某个选项时,注意header的菜单栏和shadow菜单栏的选中状态同步
		switch (checkedId) {
		case R.id.rb_retweets:
			rb_retweets.setChecked(true);
			shadow_rb_retweets.setChecked(true);
			break;
		case R.id.rb_comments:
			rb_comments.setChecked(true);
			shadow_rb_comments.setChecked(true);
			break;
		case R.id.rb_likes:
			rb_likes.setChecked(true);
			shadow_rb_likes.setChecked(true);
			break;
		default:
			break;
		}
	}
	
}
