package org.hyb.weibo.utils;

import org.hyb.weibo.R;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.graphics.Bitmap;

public class ImageOptHelper {
	public static DisplayImageOptions getImgOptions() {
		DisplayImageOptions imgOptions = new DisplayImageOptions.Builder()
			.cacheOnDisc()//设置下载的图片是否缓存在SD卡中 
			.cacheInMemory()//设置下载的图片是否缓存在内存中
			.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
			.showStubImage(R.drawable.timeline_image_loading)
			.showImageForEmptyUri(R.drawable.timeline_image_loading)
			.showImageOnFail(R.drawable.timeline_image_failure)
			.build();
		return imgOptions;
	}
	
	public static DisplayImageOptions getAvatarOptions() {
		DisplayImageOptions	avatarOptions = new DisplayImageOptions.Builder()
			.cacheOnDisc()
			.cacheInMemory()
			.bitmapConfig(Bitmap.Config.RGB_565)
			.showStubImage(R.drawable.avatar_default)
			.showImageForEmptyUri(R.drawable.avatar_default)
			.showImageOnFail(R.drawable.avatar_default)
			.displayer(new RoundedBitmapDisplayer(999))//是否设置为圆角，弧度为多少，这里999代表图片是圆形
			.build();
		return avatarOptions;
	}
	//根据传入的圆角弧度获取option
	public static DisplayImageOptions getCornerOptions(int cornerRadiusPixels) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
			.cacheOnDisc()
			.cacheInMemory()
			.bitmapConfig(Bitmap.Config.RGB_565)
			.showStubImage(R.drawable.timeline_image_loading)
			.showImageForEmptyUri(R.drawable.timeline_image_loading)
			.showImageOnFail(R.drawable.timeline_image_loading)
			.displayer(new RoundedBitmapDisplayer(cornerRadiusPixels)).build();
		return options;
	}
}
