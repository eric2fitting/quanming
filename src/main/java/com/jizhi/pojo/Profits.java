package com.jizhi.pojo;

public class Profits {
	private Integer id;
	private Integer userId;//卖家id
	private Double animalProfit;
	private Double shareProfit;
	private Integer NFC;
	private Integer sharerId;//分享给卖家的人的id
	
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
	public Integer getNFC() {
		return NFC;
	}
	public void setNFC(Integer nFC) {
		NFC = nFC;
	}
	public Integer getSharerId() {
		return sharerId;
	}
	public void setSharerId(Integer sharerId) {
		this.sharerId = sharerId;
	}

}
