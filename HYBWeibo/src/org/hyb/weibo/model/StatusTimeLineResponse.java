package org.hyb.weibo.model;

import java.util.ArrayList;

public class StatusTimeLineResponse extends BaseModel {
	private ArrayList<Status> statuses;
	private int total_number;

	public ArrayList<Status> getStatuses() {
		return statuses;
	}

	public void setStatuses(ArrayList<Status> statuses) {
		this.statuses = statuses;
	}

	public int getTotal_number() {
		return total_number;
	}

	public void setTotal_number(int total_number) {
		this.total_number = total_number;
	}
}
