package com.jizhi.pojo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Profits {
	private Integer id;
	private Integer userId;//卖家id
	private Double animalProfit;
	private Double shareProfit;
	private Double NFC;
	private Integer sharerId;//分享给卖家的人的id
	private Integer state;
	private Date updateTime;
	
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Double getAnimalProfit() {
		return animalProfit;
	}
	public void setAnimalProfit(Double animalProfit) {
		this.animalProfit = animalProfit;
	}
	public Double getShareProfit() {
		return shareProfit;
	}
	public void setShareProfit(Double shareProfit) {
		this.shareProfit = shareProfit;
	}
	public Double getNFC() {
		return NFC;
	}
	public void setNFC(Double nFC) {
		NFC = nFC;
	}
	public Integer getSharerId() {
		return sharerId;
	}
	public void setSharerId(Integer sharerId) {
		this.sharerId = sharerId;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}

}
