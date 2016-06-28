package org.hyb.weibo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hyb.weibo.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 处理微博正文文本的工具类，把@、#话题#和emoji表情转成SpannableString字符串
 * */


public class StringUtils {
	public static SpannableString getWeiboContent(final Context context, final TextView tv, String source) {
		String regexAt = "@[\u4e00-\u9fa5\\w]+";//@字符串的正则
		String regexTopic = "#[\u4e00-\u9fa5\\w]+#";//#话题#的正则
		String regexEmoji = "\\[[\u4e00-\u9fa5\\w]+\\]";//emoji表情的正则，例如[微笑]
		//有括号括起每个正则,然后有'|'串起来,这样的话一次就可以查看是否满足三个正则中的其中一个
		String regex = "(" + regexAt + ")|(" + regexTopic + ")|(" + regexEmoji + ")";
		
		SpannableString spannableString = new SpannableString(source);
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(spannableString);
		
		if(matcher.find()) {
			tv.setMovementMethod(LinkMovementMethod.getInstance());
			matcher.reset();
		}
		
		while(matcher.find()) {
			//找到匹配的字符串,1就是第一个括号里的正则,matcher.group(1)获取的是满足第一个括号里的正则的字符串
			final String atStr = matcher.group(1);
			final String topicStr = matcher.group(2);
			String emojiStr = matcher.group(3);
			if(atStr != null) {
				int start = matcher.start(1);
				HYBClickableSpan clickableSpan = new HYBClickableSpan(context) {
					@Override
					public void onClick(View widget) {
						ToastUtils.showToast(context, "用户: " + atStr, Toast.LENGTH_SHORT);
					}
				};
				//给@字符串添加可点击效果,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE --- 不包含两端start和end
				spannableString.setSpan(clickableSpan, start, start + atStr.length(), 
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			
			if(topicStr != null) {
				int start = matcher.start(2);
				
				HYBClickableSpan clickableSpan = new HYBClickableSpan(context) {
					
					@Override
					public void onClick(View widget) {
						ToastUtils.showToast(context, "话题: " + topicStr, Toast.LENGTH_SHORT);
					}
				};
				spannableString.setSpan(clickableSpan, start, start + topicStr.length(), 
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			
			if(emojiStr != null) {
				int start = matcher.start(3);
				
				int imgRes = EmotionUtils.getImgByName(emojiStr);
				Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgRes);
				
				if(bitmap != null) {
					int size = 2 * (int) tv.getTextSize();
					//根据textview的textsize大小来缩放表情图片
					bitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
					
					ImageSpan imageSpan = new ImageSpan(context, bitmap);
					spannableString.setSpan(imageSpan, start, start + emojiStr.length(), 
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
			
		}
		
		return spannableString;
	}
	//因为默认的ClickableSpan会给可点击的富文本添加默认的颜色,想要修改成自己的颜色需要新建一个集成ClickableSpan的类,
	//并重写updateDrawState方法
	static class HYBClickableSpan extends ClickableSpan {

		private Context context;
		
		public HYBClickableSpan(Context context) {
			this.context = context;
		}

		@Override
		public void onClick(View widget) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setColor(context.getResources().getColor(R.color.txt_at_blue));
			ds.setUnderlineText(false);
		}
		
		
	}
}
