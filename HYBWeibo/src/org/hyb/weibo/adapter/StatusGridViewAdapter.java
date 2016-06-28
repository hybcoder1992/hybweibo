package org.hyb.weibo.adapter;

import java.util.List;

import org.hyb.weibo.R;
import org.hyb.weibo.model.PicUrls;

import com.nostra13.universalimageloader.core.ImageLoader;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

public class StatusGridViewAdapter extends BaseAdapter {
	private Context context;
	private List<PicUrls> datas;
	private ImageLoader imageLoader;
	public StatusGridViewAdapter(Context context,List<PicUrls> datas)
	{
		this.context=context;
		this.datas=datas;
		this.imageLoader=ImageLoader.getInstance();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	@Override
	public PicUrls getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView==null)
		{
			convertView=View.inflate(context, R.layout.item_grid_image, null);
			holder=new ViewHolder();
			holder.iv_image=(ImageView)convertView.findViewById(R.id.iv_image);
			convertView.setTag(holder);
		}
		else {
			holder=(ViewHolder)convertView.getTag();
		}
		GridView gv = (GridView) parent;
		int horizontalSpacing = gv.getHorizontalSpacing();
		int numColumns = gv.getNumColumns();
		int itemWidth = (gv.getWidth() - (numColumns-1) * horizontalSpacing
				- gv.getPaddingLeft() - gv.getPaddingRight()) / numColumns;

		LayoutParams params = new LayoutParams(itemWidth, itemWidth);
		holder.iv_image.setLayoutParams(params);
		
		PicUrls urls = getItem(position);
		imageLoader.displayImage(urls.getThumbnail_pic(), holder.iv_image);
		return convertView;
	}
	class ViewHolder
	{
		public ImageView iv_image;
	}
}
