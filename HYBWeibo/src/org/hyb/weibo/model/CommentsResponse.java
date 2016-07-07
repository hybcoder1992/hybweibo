package org.hyb.weibo.model;

import java.util.List;

public class CommentsResponse {
	private int total_number;
	private List<Comment> comments;
	public int getTotal_number() {
		return total_number;
	}
	public void setTotal_num(int total_num) {
		this.total_number = total_num;
	}
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
}
