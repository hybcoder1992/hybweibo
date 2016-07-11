package org.hyb.weibo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

public class ImageUtils {
	public static final int GET_IMAGE_BY_CAMERA = 5001;
	public static final int GET_IMAGE_FROM_PHONE = 5002;
	public static final int CROP_IMAGE = 5003;
	public static Uri imageUriFromCamera;
	public static Uri cropImageUri;
	public static void openCameraImage(final Activity activity)
	{
		ImageUtils.imageUriFromCamera=ImageUtils.createImagePathUri(activity);
		Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// MediaStore.EXTRA_OUTPUT参数不设置时,系统会自动生成一个uri,但是只会返回一个缩略图
		intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUtils.imageUriFromCamera);
		activity.startActivityForResult(intent, GET_IMAGE_BY_CAMERA);
	}
	public static void openLocalImage(final Activity activity) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		activity.startActivityForResult(intent, ImageUtils.GET_IMAGE_FROM_PHONE);
	}
	/**
	 * 创建一条图片地址uri,用于保存拍照后的照片
	 * 
	 * @param context
	 * @return 图片的uri
	 */
	private static Uri createImagePathUri(Context context)
	{
		Uri imageFilePath=null;
		String status=Environment.getExternalStorageState();
		SimpleDateFormat timeFormat=new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
		long time=System.currentTimeMillis();
		String imageName=timeFormat.format(new Date(time));
		ContentValues values=new ContentValues(3);
		values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName);
		values.put(MediaStore.Images.Media.DATE_TAKEN,time);
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		if(status.equals(Environment.MEDIA_MOUNTED)){
			//判断是否有sd卡,优先使用sd卡存储
			imageFilePath=context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			
		}else {
			imageFilePath=context.getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
		}
		return imageFilePath;
	}
	/**
	 * 删除一条图片
	 */
	public static void deleteImageUri(Context context, Uri uri) {
		context.getContentResolver().delete(uri, null, null);
	}
	public static void cropImage(Activity activity, Uri srcUri) {
		ImageUtils.cropImageUri=ImageUtils.createImagePathUri(activity);
		Intent intent=new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(srcUri, "image/*");
		intent.putExtra("crop", true);
		////////////////////////////////////////////////////////////////
		// 1.宽高和比例都不设置时,裁剪框可以自行调整(比例和大小都可以随意调整)
		////////////////////////////////////////////////////////////////
		// 2.只设置裁剪框宽高比(aspect)后,裁剪框比例固定不可调整,只能调整大小
		////////////////////////////////////////////////////////////////
		// 3.裁剪后生成图片宽高(output)的设置和裁剪框无关,只决定最终生成图片大小
		////////////////////////////////////////////////////////////////
		// 4.裁剪框宽高比例(aspect)可以和裁剪后生成图片比例(output)不同,此时,
		//	会以裁剪框的宽为准,按照裁剪宽高比例生成一个图片,该图和框选部分可能不同,
		//  不同的情况可能是截取框选的一部分,也可能超出框选部分,向下延伸补足
		////////////////////////////////////////////////////////////////
		
		// aspectX aspectY 是裁剪框宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪后生成图片的宽高
		//intent.putExtra("outputX", 300);
		//intent.putExtra("outputY", 100);
		
		// return-data为true时,会直接返回bitmap数据,但是大图裁剪时会出现问题,推荐下面为false时的方式
		// return-data为false时,不会返回bitmap,但需要指定一个MediaStore.EXTRA_OUTPUT保存图片uri
		intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUtils.cropImageUri);
		intent.putExtra("return-data", false);
		
		activity.startActivityForResult(intent, CROP_IMAGE);	
	
	}
}
