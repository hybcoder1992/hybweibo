package org.hyb.weibo.utils;

public class URLs {
	public static String BASE_URL="https://api.weibo.com/2/";
	//首页微博列表
	public static  String statusesHome_timeline=BASE_URL+"statuses/home_timeline.json";
	// 微博评论列表
	public static	String commentsShow = BASE_URL + "comments/show.json";
		// 对一条微博进行评论
	public static	String commentsCreate = BASE_URL + "comments/create.json";
	
}
