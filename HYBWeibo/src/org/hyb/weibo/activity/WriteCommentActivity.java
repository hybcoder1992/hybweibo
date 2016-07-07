package org.hyb.weibo.activity;

import org.hyb.weibo.R;
import org.hyb.weibo.api.SimpleRequestListener;
import org.hyb.weibo.model.Status;
import org.hyb.weibo.utils.TitleBarBuilder;

import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

public class WriteCommentActivity extends BaseActivity implements OnClickListener{
	//评论输入框
	EditText et_write_status;
	//底部按钮
	ImageView iv_image;
	ImageView iv_at;
	ImageView iv_topic;
	ImageView iv_emoji;
	ImageView iv_add;
	//待评论微博
	Status status;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_write_status);
		status=(Status)getIntent().getSerializableExtra("status");
		initView();
	}
	private void initView()
	{
		new TitleBarBuilder(this).setTitleText("发评论")
		.setLeftText("取消")
		.setLeftOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WriteCommentActivity.this.finish();
			}
		})
		.setRightText("发送")
		.setRightOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendComment();
			}
		});
		et_write_status=(EditText)findViewById(R.id.et_write_status);
		iv_add=(ImageView)findViewById(R.id.iv_add);
		iv_at=(ImageView)findViewById(R.id.iv_at);
		iv_emoji=(ImageView)findViewById(R.id.iv_emoji);
		iv_image=(ImageView)findViewById(R.id.iv_image);
		iv_topic=(ImageView)findViewById(R.id.iv_topic);
		
		iv_add.setOnClickListener(this);
		iv_at.setOnClickListener(this);
		iv_emoji.setOnClickListener(this);
		iv_image.setOnClickListener(this);
		iv_topic.setOnClickListener(this);
	}
	private void sendComment()
	{
		String comment=et_write_status.getText().toString().trim();
		if(TextUtils.isEmpty(comment)){
			showToast("评论内容不能为空");
			return;
		}
		weiboApi.commentsCreate(status.getId(), comment, new SimpleRequestListener(this, null)
		{
			@Override
			public void onComplete(String response) {
				// TODO Auto-generated method stub
				super.onComplete(response);
				showToast("发送评论成功");
				//评论发送成功后,设置Result结果数据.然后关闭本页面
				Intent data=new Intent();
				data.putExtra("sendCommentSuccess", true);
				//如果back键返回,requestcode为RESULT_CANCEL
				setResult(RESULT_OK,data);
				WriteCommentActivity.this.finish();
			}
			@Override
			public void onAllDone() {
				// TODO Auto-generated method stub
				super.onAllDone();
			}
		});
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
