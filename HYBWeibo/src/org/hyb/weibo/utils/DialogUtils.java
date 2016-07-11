package org.hyb.weibo.utils;

import java.util.List;

import org.hyb.weibo.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;

public class DialogUtils {
	public static Dialog createLoadingDialog(Context context)
	{
		ProgressDialog progressDialog=new ProgressDialog(context,R.style.DialogCommon);
		return progressDialog;
	}
	/**
	 * 提示信息dialog
	 * 
	 * @param context
	 * @param title
	 *            标题名称,内容为空时即不设置标题
	 * @param msg
	 *            提示信息内容
	 * @return
	 */
	public static AlertDialog showMsgDialog(Context context,String title,String msg)
	{
		AlertDialog.Builder builder=new Builder(context);
		if(!TextUtils.isEmpty(title)){
			builder.setTitle(title);
		}
		AlertDialog dialog=builder.setMessage(msg).setNegativeButton("确定", null).show();
		return dialog;
	}
	/**
	 * 确认dialog
	 * 
	 * @param context
	 * @param title
	 *            标题名称,内容为空时即不设置标题
	 * @param msg
	 *            确认信息内容
	 * @param onClickListener
	 *            确定按钮监听
	 * @return
	 */
	public static AlertDialog showConfirmDialog(Context context,String title,String msg,DialogInterface.OnClickListener listener)
	{
		AlertDialog.Builder builder=new Builder(context);
		if(!TextUtils.isEmpty(title)){
			builder.setTitle(title);
		}
		AlertDialog dialog=builder.setMessage(msg).setPositiveButton("确定", listener)
				.setNegativeButton("取消", null).show();
		return dialog;
	}
	/**
	 * 列表型dialog
	 * 
	 * @param context
	 * @param title
	 *            标题名称,内容为空时即不设置标题
	 * @param items
	 *            所有item选项的名称
	 * @param onClickListener
	 *            确定按钮监听
	 * @return
	 */
	public static AlertDialog showListDialog(Context context,String title,List<String> items,DialogInterface.OnClickListener listener)
	{
		return showListDialog(context, title, items.toArray(new String[items.size()]), listener);
	}
	/**
	 * 列表型dialog
	 * 
	 * @param context
	 * @param title
	 *            标题名称,内容为空时即不设置标题
	 * @param items
	 *            所有item选项的名称
	 * @param onClickListener
	 *            确定按钮监听
	 * @return
	 */
	public static AlertDialog showListDialog(Context context,String title,String [] items,DialogInterface.OnClickListener listener)
	{
		AlertDialog.Builder builder=new Builder(context);
		if(!TextUtils.isEmpty(title))builder.setTitle(title);
		AlertDialog dialog=builder.setItems(items, listener).show();
		return dialog;
	}
	/**
	 * 单选框dialog
	 * 
	 * @param context
	 * @param title
	 *            标题名称,内容为空时即不设置标题
	 * @param items
	 *            所有item选项的名称
	 * @param defaultItemIndex
	 *            默认选项
	 * @param onClickListener
	 *            确定按钮监听
	 * @return
	 */
	public static AlertDialog showSingleChoiceDialog(Context context,String title,String [] items,int defaultItemIndex,DialogInterface.OnClickListener listener)
	{
		AlertDialog.Builder builder=new Builder(context);
		if(!TextUtils.isEmpty(title))builder.setTitle(title);
		AlertDialog dialog=builder.setSingleChoiceItems(items, defaultItemIndex, listener)
				.setNegativeButton("取消", null).show();
		return dialog;
		
	}
	/**
	 * 复选框对话框
	 * 
	 * @param context
	 * @param title
	 *            标题名称,内容为空时即不设置标题
	 * @param items
	 *            所有item选项的名称
	 * @param defaultCheckedItems
	 *            初始化选择,和items同长度,true代表对应位置选中,如{true, true,
	 *            false}代表第一二项选中,第三项不选中
	 * @param onMultiChoiceClickListener
	 *            多选监听
	 * @param onClickListener
	 *            确定按钮监听
	 * @return
	 */
	public static AlertDialog showMultiChoiceDialog(Context context,String title,String [] items,
			boolean[] defaultCheckedItems,DialogInterface.OnMultiChoiceClickListener multiChoiceListener, 
			DialogInterface.OnClickListener listener)
	{
		AlertDialog.Builder builder=new Builder(context);
		if(!TextUtils.isEmpty(title))builder.setTitle(title);
		AlertDialog dialog=builder.setMultiChoiceItems(items, defaultCheckedItems, multiChoiceListener)
				.setPositiveButton("确定", listener)
				.setNegativeButton("取消", null).show();
		return dialog;
		
	}
	public static AlertDialog showImagePickDialog(final Activity activity)
	{
		String title="选择获取图片的方式";
		String [] items=new String []{"拍照","从相册中选取"};
		return showListDialog(activity, title, items, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				switch (which) {
				case 0:
					ImageUtils.openCameraImage(activity);
					break;
				case 1:
					ImageUtils.openLocalImage(activity);
					break;
				default:
					break;
				}
			}
		});
	}
	
}
