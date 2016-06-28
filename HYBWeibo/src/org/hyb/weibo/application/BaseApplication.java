package org.hyb.weibo.application;

import org.hyb.weibo.utils.ImageOptHelper;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application {
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		initImageLoader(this);
	}
	// 初始化图片处理
		private void initImageLoader(Context context) {
			// This configuration tuning is custom. You can tune every option, you
			// may tune some of them,
			// or you can create default configuration by
			// ImageLoaderConfiguration.createDefault(this);
			// method.
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
					.threadPriority(Thread.NORM_PRIORITY - 2)
					.discCacheFileNameGenerator(new Md5FileNameGenerator())//将缓存下来的文件以MD5方式命名
					.tasksProcessingOrder(QueueProcessingType.LIFO)
					.defaultDisplayImageOptions(ImageOptHelper.getImgOptions())
					.build();
			// Initialize ImageLoader with configuration.
			ImageLoader.getInstance().init(config);
		}
}
