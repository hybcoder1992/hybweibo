package org.hyb.weibo.adapter;

import java.util.ArrayList;
import java.util.List;

import org.hyb.weibo.R;
import org.hyb.weibo.activity.StatusDetailActivity;
import org.hyb.weibo.activity.WriteCommentActivity;
import org.hyb.weibo.activity.WriteStatusActivity;
import org.hyb.weibo.model.PicUrls;
import org.hyb.weibo.model.Status;
import org.hyb.weibo.model.User;
import org.hyb.weibo.utils.DateUtils;
import org.hyb.weibo.utils.StringUtils;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StatusAdapter extends BaseAdapter {
	private Context context;
	private List<Status> datas;
	ImageLoader imageLoader;
	public static class ViewHolder {
		public LinearLayout ll_card_content;//对应layout文件item_status.xml最外层的LinearLayout
		public ImageView iv_avatar;//头像
		public RelativeLayout rl_content;//包含tv_subhead和tv_caption这两个TextView
		public TextView tv_subhead;//用户名称
		public TextView tv_caption;

		public TextView tv_content;//微博正文
		public FrameLayout include_status_image;//微博图片,包含了下面的gv_images和iv_image
		public GridView gv_images;
		public ImageView iv_image;

		public LinearLayout include_retweeted_status;
		public TextView tv_retweeted_content;
		public FrameLayout include_retweeted_status_image;
		public GridView gv_retweeted_images;
		public ImageView iv_retweeted_image;

		public LinearLayout ll_share_bottom;//底部控制栏的转发按钮
		public ImageView iv_share_bottom;
		public TextView tv_share_bottom;
		public LinearLayout ll_comment_bottom;//底部控制栏的评论按钮
		public ImageView iv_comment_bottom;
		public TextView tv_comment_bottom;
		public LinearLayout ll_like_bottom;//底部控制栏的点赞按钮
		public ImageView iv_like_bottom;
		public TextView tv_like_bottom;
	}
	public StatusAdapter(Context context,List<Status> datas)
	{
		this.context=context;
		this.datas=datas;
		imageLoader=ImageLoader.getInstance();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public Status getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if(convertView==null)
		{
			holder=new ViewHolder();
			convertView = View.inflate(context, R.layout.item_status, null);
			holder.ll_card_content = (LinearLayout) convertView
					.findViewById(R.id.ll_card_content);
			holder.iv_avatar = (ImageView) convertView
					.findViewById(R.id.iv_avatar);
			holder.rl_content = (RelativeLayout) convertView
					.findViewById(R.id.rl_content);
			holder.tv_subhead = (TextView) convertView
					.findViewById(R.id.tv_subhead);
			holder.tv_caption = (TextView) convertView
					.findViewById(R.id.tv_caption);

			holder.tv_content = (TextView) convertView
					.findViewById(R.id.tv_content);
			holder.include_status_image = (FrameLayout) convertView
					.findViewById(R.id.include_status_image);
			holder.gv_images = (GridView) holder.include_status_image
					.findViewById(R.id.gv_images);
			holder.iv_image = (ImageView) holder.include_status_image
					.findViewById(R.id.iv_image);

			holder.include_retweeted_status = (LinearLayout) convertView
					.findViewById(R.id.include_retweeted_status);
			holder.tv_retweeted_content = (TextView) holder.include_retweeted_status
					.findViewById(R.id.tv_retweeted_content);
			holder.include_retweeted_status_image = (FrameLayout) holder.include_retweeted_status
					.findViewById(R.id.include_status_image);
			holder.gv_retweeted_images = (GridView) holder.include_retweeted_status_image
					.findViewById(R.id.gv_images);
			holder.iv_retweeted_image = (ImageView) holder.include_retweeted_status_image
					.findViewById(R.id.iv_image);

			holder.ll_share_bottom = (LinearLayout) convertView
					.findViewById(R.id.ll_share_bottom);
			holder.iv_share_bottom = (ImageView) convertView
					.findViewById(R.id.iv_share_bottom);
			holder.tv_share_bottom = (TextView) convertView
					.findViewById(R.id.tv_share_bottom);
			holder.ll_comment_bottom = (LinearLayout) convertView
					.findViewById(R.id.ll_comment_bottom);
			holder.iv_comment_bottom = (ImageView) convertView
					.findViewById(R.id.iv_comment_bottom);
			holder.tv_comment_bottom = (TextView) convertView
					.findViewById(R.id.tv_comment_bottom);
			holder.ll_like_bottom = (LinearLayout) convertView
					.findViewById(R.id.ll_like_bottom);
			holder.iv_like_bottom = (ImageView) convertView
					.findViewById(R.id.iv_like_bottom);
			holder.tv_like_bottom = (TextView) convertView
					.findViewById(R.id.tv_like_bottom);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		final Status status=getItem(position);
		User user=status.getUser();
		imageLoader.displayImage(user.getProfile_image_url(), holder.iv_avatar);
		holder.tv_subhead.setText(user.getName());
		holder.tv_caption.setText(DateUtils.getShortTime(status.getCreated_at())+" 来自 "+Html.fromHtml(status.getSource()));
		holder.tv_content.setText(StringUtils.getWeiboContent(context, holder.tv_content, status.getText()));
		setImages(status, holder.include_status_image, holder.gv_images, holder.iv_image);
		Status retweeted_status=status.getRetweeted_status();
		if(retweeted_status!=null)
		{
			User retUser=retweeted_status.getUser();
			holder.include_retweeted_status.setVisibility(View.VISIBLE);
			String retweeted_content="@"+retUser.getName()+":"+retweeted_status.getText();
			holder.tv_retweeted_content.setText(StringUtils.getWeiboContent(context, holder.tv_retweeted_content, retweeted_content));
			setImages(retweeted_status, holder.include_retweeted_status_image, holder.gv_retweeted_images, holder.iv_retweeted_image);
		}
		else
			holder.include_retweeted_status.setVisibility(View.GONE);
		holder.tv_share_bottom.setText(status.getReposts_count() == 0 ?
				"转发" : status.getReposts_count() + "");
		
		holder.tv_comment_bottom.setText(status.getComments_count() == 0 ?
				"评论" : status.getComments_count() + "");
		
		holder.tv_like_bottom.setText(status.getAttitudes_count() == 0 ?
				"赞" : status.getAttitudes_count() + "");
		holder.tv_share_bottom.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d("hyb", "转发按钮click");
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,WriteStatusActivity.class);
				intent.putExtra("status", status);
				context.startActivity(intent);
			}
		});
		//给每条微博内容添加点击事件,跳转到对应微博的详情页面
		
		holder.ll_card_content.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,StatusDetailActivity.class);
				//Log.d("hyb", "---onclick---");
				intent.putExtra("status", status);
				context.startActivity(intent);
			}
		});
		
		holder.include_retweeted_status.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(context,StatusDetailActivity.class);
				//Log.d("hyb", "---onclick---");
				intent.putExtra("status", status);
				context.startActivity(intent);
			}
		});
		holder.ll_comment_bottom.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(status.getComments_count()>0)
				{
					Intent intent=new Intent(context,StatusDetailActivity.class);
					intent.putExtra("status", status);
					intent.putExtra("scroll2Comment", true);
					context.startActivity(intent);
				}
				else{
					//跳转到写评论的activity
					Intent intent=new Intent(context,WriteCommentActivity.class);
					intent.putExtra("status", status);
					
					context.startActivity(intent);
				}
			}
		});
		
		return convertView;
	}
	private void setImages(Status status,FrameLayout imgContainer,GridView gv_images,ImageView iv_image)
	{
		ArrayList<PicUrls> pic_urls=status.getPic_urls();
		//String thumnail_pic=status.getThumbnail_pic();
		String thumnail_pic=status.getBmiddle_pic();
		if(pic_urls!=null && pic_urls.size()>1)
		{
			imgContainer.setVisibility(View.VISIBLE);
			gv_images.setVisibility(View.VISIBLE);
			iv_image.setVisibility(View.GONE);
			StatusGridViewAdapter gridViewAdapter=new StatusGridViewAdapter(context, pic_urls);
			gv_images.setAdapter(gridViewAdapter);
		}
		else if(thumnail_pic!=null)
		{
			imgContainer.setVisibility(View.VISIBLE);
			gv_images.setVisibility(View.GONE);
			iv_image.setVisibility(View.VISIBLE);
			imageLoader.displayImage(thumnail_pic, iv_image);
		}
		else {
			imgContainer.setVisibility(View.GONE);
		}
	}
}
