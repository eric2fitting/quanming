package com.jizhi.pojo.vo;

import java.io.Serializable;

public class IsOrderOrOverTime implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer orderTimeId;
	private String startTime;
	private String endTime;
	private String state;
	public Integer getOrderTimeId() {
		return orderTimeId;
	}
	public void setOrderTimeId(Integer orderTimeId) {
		this.orderTimeId = orderTimeId;
	}

	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
}
