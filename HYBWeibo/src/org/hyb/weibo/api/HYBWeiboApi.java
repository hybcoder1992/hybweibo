package org.hyb.weibo.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.hyb.weibo.constants.AccessTokenKeeper;
import org.hyb.weibo.utils.URLs;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboParameters;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.WeiboAPI;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

public class HYBWeiboApi extends WeiboAPI {
	//这个handler实在子线程中创建,但它需要在主线程跟新ui,所有构造函数传入主线程的looper
	private Handler mainLooperHandler=new Handler(Looper.getMainLooper());
	public HYBWeiboApi(Oauth2AccessToken oauth2AccessToken) {
		super(oauth2AccessToken);
		// TODO Auto-generated constructor stub
	}
	
	public void requestInMainLooper(String url, WeiboParameters params, String httpMethod, final RequestListener listener)
	{
		request(url, params, httpMethod, new RequestListener() {
			
			@Override
			public void onIOException(final IOException e) {
				// TODO Auto-generated method stub
				//遇到ioexception异常时,post到主线程处理
				mainLooperHandler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						listener.onIOException(e);
					}
				});
			}
			
			@Override
			public void onError(final WeiboException e) {
				// TODO Auto-generated method stub
				mainLooperHandler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						listener.onError(e);
					}
				});
			}
			
			@Override
			public void onComplete4binary(final ByteArrayOutputStream responseOS) {
				// TODO Auto-generated method stub
				mainLooperHandler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						listener.onComplete4binary(responseOS);
					}
				});
			}
			
			@Override
			public void onComplete(final String response) {
				// TODO Auto-generated method stub
				mainLooperHandler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						listener.onComplete(response);
					}
				});
			}
		});
	}
	@Override
	protected void request(String url, WeiboParameters params, String httpMethod, RequestListener listener) {
		// TODO Auto-generated method stub
		super.request(url, params, httpMethod, listener);
	}
	public HYBWeiboApi(Context context)
	{
		this(AccessTokenKeeper.readAccessToken(context));
	}
	
	/**
	 * 获取当前登录用户及其所关注用户的最新微博
	 * 
	 * @param page
	 *            返回结果的页码。(单页返回的记录条数，默认为20。)
	 * @param listener
	 */
	public void statusesHome_timeline(long page, RequestListener listener) {
		WeiboParameters parameters = new WeiboParameters();
		parameters.add("page", page);
		//parameters.add("count", 1);
		requestInMainLooper(URLs.statusesHome_timeline, parameters, HTTPMETHOD_GET, listener);
	}
	
	/**
	 * 根据微博id返回某条微博的评论列表
	 * @param id 需要查询的微博id
	 * @param page 返回结果的页码
	 * @param listener 
	 */
	public void commentsShow(long id,long page,RequestListener listener)
	{
		WeiboParameters parameters = new WeiboParameters();
		parameters.add("id", id);
		parameters.add("page", page);
		requestInMainLooper(URLs.commentsShow, parameters, HTTPMETHOD_GET, listener);
	}
	/**
	 * 对一条微博进行评论
	 * id:需要评论的微博id
	 * comment:评论的内容
	 * */
	public void commentsCreate(long id,String comment,RequestListener listener)
	{
		WeiboParameters parameters=new WeiboParameters();
		parameters.add("id", id);
		parameters.add("comment", comment);
		requestInMainLooper(URLs.commentsCreate, parameters, HTTPMETHOD_POST, listener);
	}
}
