package org.hyb.weibo.activity;

import java.util.ArrayList;

import org.hyb.weibo.R;
import org.hyb.weibo.adapter.StatusGridViewAdapter;
import org.hyb.weibo.model.PicUrls;
import org.hyb.weibo.model.Status;
import org.hyb.weibo.model.User;
import org.hyb.weibo.utils.DateUtils;
import org.hyb.weibo.utils.TitleBarBuilder;
import org.hyb.weibo.widget.WrapHeightGridView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
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
		
		PullToRefreshListView plv_status_detail;
	
	private Status status;//详情页的微博信息
	@Override
	protected void onStart() {
	// TODO Auto-generated method stub
		super.onStart();
		Log.d("hyb", "---onStart---");
	}
	@Override
	protected void onResume() {
	// TODO Auto-generated method stub
		super.onResume();
		Log.d("hyb", "---onResume---");
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_status_detail);
		Log.d("hyb", "---detail activity---");
		status=(Status)getIntent().getSerializableExtra("status");
		initView();
		initData();
	}
	
	private void initView()
	{
		initTitle();
		initDetailHead();
		initTab();
		initListView();
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
	//初始化菜单栏
	private void initTab()
	{
		// shadow悬浮栏
		shadow_status_detail_tab = findViewById(R.id.status_detail_tab);
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
		plv_status_detail=(PullToRefreshListView)findViewById(R.id.plv_status_detail);
		final ListView lv=plv_status_detail.getRefreshableView();
		lv.addHeaderView(status_detail_info);
		lv.addHeaderView(status_detail_tab);
		
	}
	private void initData()
	{
		//设置listview的headerview数据--微博数据
		User user=status.getUser();
		imgLoader.displayImage(user.getProfile_image_url(), iv_avatar);
		tv_subhead.setText(user.getName());
		tv_caption.setText(DateUtils.getShortTime(status.getCreated_at()+" 来自"+Html.fromHtml(status.getSource())));
		Log.d("hyb", "初始化数据");
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
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		
	}
	
}
