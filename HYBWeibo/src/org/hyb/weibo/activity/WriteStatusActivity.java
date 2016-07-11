package org.hyb.weibo.activity;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;

import org.hyb.weibo.R;
import org.hyb.weibo.adapter.WriteStatusGridImgsAdapter;
import org.hyb.weibo.model.Status;
import org.hyb.weibo.utils.DialogUtils;
import org.hyb.weibo.utils.ImageUtils;
import org.hyb.weibo.utils.StringUtils;
import org.hyb.weibo.utils.TitleBarBuilder;
import org.hyb.weibo.widget.WrapHeightGridView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WriteStatusActivity extends BaseActivity implements OnClickListener,OnItemClickListener{
	//输入框
	private EditText et_write_status;
	//添加的九宫格图片
	private WrapHeightGridView gv_write_status;
	//转发微博内容
	private View include_retweeted_status_card;
	private ImageView iv_rstatus_img;
	private TextView tv_rstatus_username;
	private TextView tv_rstatus_content;
	//底部添加栏
	private ImageView iv_image;
	private ImageView iv_at;
	private ImageView iv_topic;
	private ImageView iv_emoji;
	private ImageView iv_add;
	//表情选择面板
	private LinearLayout ll_emotion_dashboard;
	private ViewPager vp_emotion_dashboard;
	//进度框
	private ProgressDialog progressDialog;
	private WriteStatusGridImgsAdapter statusGridImgsAdapter;
	//引用的微博
	private Status retweeted_status;
	//显示在页面中,实际需要转发的微博内容
	private Status cardStatus;
	private ArrayList<Uri> imgUris = new ArrayList<Uri>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_write_status);
		retweeted_status=(Status)getIntent().getSerializableExtra("status");
		initView();
	}
	private void initView()
	{
		//设置标题栏
		new TitleBarBuilder(this)
		.setTitleText("发微博")
		.setLeftText("取消")
		.setLeftOnClickListener(this)
		.setRightText("发送")
		.setRightOnClickListener(this)
		.build();
		// 输入框
		et_write_status = (EditText) findViewById(R.id.et_write_status);
		// 添加的九宫格图片
		gv_write_status = (WrapHeightGridView) findViewById(R.id.gv_write_status);
		// 转发微博内容
		include_retweeted_status_card = findViewById(R.id.include_retweeted_status_card);
		iv_rstatus_img = (ImageView) findViewById(R.id.iv_rstatus_img);
		tv_rstatus_username = (TextView) findViewById(R.id.tv_rstatus_username);
		tv_rstatus_content = (TextView) findViewById(R.id.tv_rstatus_content);
		// 底部添加栏
		iv_image = (ImageView) findViewById(R.id.iv_image);
		iv_at = (ImageView) findViewById(R.id.iv_at);
		iv_topic = (ImageView) findViewById(R.id.iv_topic);
		iv_emoji = (ImageView) findViewById(R.id.iv_emoji);
		iv_add = (ImageView) findViewById(R.id.iv_add);
		// 表情选择面板
		ll_emotion_dashboard = (LinearLayout) findViewById(R.id.ll_emotion_dashboard);
		vp_emotion_dashboard = (ViewPager) findViewById(R.id.vp_emotion_dashboard);
		// 进度框
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("微博发布中...");
		statusGridImgsAdapter=new WriteStatusGridImgsAdapter(this, imgUris, gv_write_status);
		gv_write_status.setAdapter(statusGridImgsAdapter);
		gv_write_status.setOnItemClickListener(this);
		
		iv_add.setOnClickListener(this);
		iv_at.setOnClickListener(this);
		iv_emoji.setOnClickListener(this);
		iv_topic.setOnClickListener(this);
		iv_image.setOnClickListener(this);
		initRetweetedStatus();
	}
	//初始化引用微博内容
	private void initRetweetedStatus()
	{
		if(retweeted_status!=null)
		{
			Status rrStatus=retweeted_status.getRetweeted_status();
			if(rrStatus!=null)
			{
				String content="//@"+retweeted_status.getUser().getName()+":"+retweeted_status.getText();
				et_write_status.setText(StringUtils.getWeiboContent(this, et_write_status, content));
				//如果引用的为转发微博,则使用它转发的内容
				cardStatus=rrStatus;
			}
			else {
				et_write_status.setText("转发微博");
				cardStatus=retweeted_status;
			}
			//设置转发图片内容
			String imgUrl=cardStatus.getThumbnail_pic();
			if(TextUtils.isEmpty(imgUrl))
			{
				iv_rstatus_img.setVisibility(View.GONE);
			}else {
				iv_rstatus_img.setVisibility(View.VISIBLE);
				imgLoader.displayImage(imgUrl, iv_rstatus_img);
			}
			//设置转发文字内容
			tv_rstatus_username.setText("@"+cardStatus.getUser().getName());
			tv_rstatus_content.setText(cardStatus.getText());
			//转发微博时不能添加图片
			iv_image.setVisibility(View.GONE);
			include_retweeted_status_card.setVisibility(View.VISIBLE);
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.titlebar_tv_left:
			finish();
			break;
		case R.id.iv_image:
			DialogUtils.showImagePickDialog(this);
		
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		Object itemAdapter=parent.getAdapter();
		if(itemAdapter instanceof WriteStatusGridImgsAdapter)
		{
			if(position==statusGridImgsAdapter.getCount()-1)
			{
				//如果点击的是最后一个加号图标,则显示选择图片对话框
				DialogUtils.showImagePickDialog(this);
			}
		}
	}
	//跟新显示图片
	private void updateImages()
	{
		if(imgUris.size()>0)
		{
			gv_write_status.setVisibility(View.VISIBLE);
			statusGridImgsAdapter.notifyDataSetChanged();
		}
		else {
			//无图则不显示gridview
			gv_write_status.setVisibility(View.GONE);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ImageUtils.GET_IMAGE_BY_CAMERA:
			if(resultCode==RESULT_CANCELED)
			{
				//如果拍照取消,将之前增加的图片地址删除
				ImageUtils.deleteImageUri(this, ImageUtils.imageUriFromCamera);
			}else {
				ImageUtils.cropImage(this, ImageUtils.imageUriFromCamera);
			}
			break;
		case ImageUtils.CROP_IMAGE:
			if(resultCode!=RESULT_CANCELED)
			{
				imgUris.add(ImageUtils.cropImageUri);
				updateImages();
			}
			break;
		case ImageUtils.GET_IMAGE_FROM_PHONE:
			if(resultCode!=RESULT_CANCELED)
			{
				//本地相册选择完了将图片添加到页面上
				ImageUtils.cropImage(this, data.getData());
				//imgUris.add();
				//updateImages();
			}
			break;
		default:
			break;
		}
	}
}
