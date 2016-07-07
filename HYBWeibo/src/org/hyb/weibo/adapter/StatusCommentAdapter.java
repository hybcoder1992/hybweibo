package org.hyb.weibo.adapter;

import java.util.List;

import org.hyb.weibo.R;
import org.hyb.weibo.model.Comment;
import org.hyb.weibo.model.User;
import org.hyb.weibo.utils.DateUtils;
import org.hyb.weibo.utils.StringUtils;
import org.hyb.weibo.utils.ToastUtils;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.text.SpannableString;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StatusCommentAdapter extends BaseAdapter {
	Context context;
	List<Comment> comments;
	ImageLoader imageLoader;
	
	public StatusCommentAdapter(Context context, List<Comment> comments) {
		super();
		this.context = context;
		this.comments = comments;
		this.imageLoader=ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return comments.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return comments.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView==null)
		{
			holder=new ViewHolder();
			convertView=View.inflate(context, R.layout.item_comment, null);
			holder.ll_comments=(LinearLayout)convertView.findViewById(R.id.ll_comment);
			holder.iv_avatar=(ImageView)convertView.findViewById(R.id.iv_avatar);
			holder.tv_subhead=(TextView)convertView.findViewById(R.id.tv_subhead);
			holder.tv_caption=(TextView)convertView.findViewById(R.id.tv_caption);
			holder.tv_comment=(TextView)convertView.findViewById(R.id.tv_comment);
			convertView.setTag(holder);
		}
		else {
			holder=(ViewHolder)convertView.getTag();
		}
		Comment comment=comments.get(position);
		User user=comment.getUser();
		imageLoader.displayImage(user.getProfile_image_url(), holder.iv_avatar);
		holder.tv_subhead.setText(user.getName());
		holder.tv_caption.setText(DateUtils.getShortTime(comment.getCreated_at()));
		SpannableString content=StringUtils.getWeiboContent(context, holder.tv_comment, comment.getText());
		holder.tv_comment.setText(content);
		holder.ll_comments.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ToastUtils.showToast(context, "点击了评论", Toast.LENGTH_SHORT);
			}
		});
		return convertView;
	}
	class ViewHolder{
		public LinearLayout ll_comments;
		public ImageView iv_avatar;
		public TextView tv_subhead;
		public TextView tv_caption;
		public TextView tv_comment;
	}

}
