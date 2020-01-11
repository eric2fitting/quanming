package com.jizhi.pojo.vo;

import java.io.Serializable;

public class BuyingDetail implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String number;//区块编码
	private Integer matchId;//订单id
	private String animalType;//动物类型
	private String size;//动物大小
	private Double price;//价格
	private String cycleProfit;//周期收益，几天涨百分之几十+买之后的卖价
	private String sellTime;//多久后卖
	private String lastPayTime;//最后付款时间
	private String buyerDate;
	private Integer isPaid;//是否付款0没有，1付款且确认了
	
	public Integer getIsPaid() {
		return isPaid;
	}
	public void setIsPaid(Integer isPaid) {
		this.isPaid = isPaid;
	}
	public Integer getMatchId() {
		return matchId;
	}
	public void setMatchId(Integer matchId) {
		this.matchId = matchId;
	}
	public String getAnimalType() {
		return animalType;
	}
	public void setAnimalType(String animalType) {
		this.animalType = animalType;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getCycleProfit() {
		return cycleProfit;
	}
	public void setCycleProfit(String cycleProfit) {
		this.cycleProfit = cycleProfit;
	}
	public String getSellTime() {
		return sellTime;
	}
	public void setSellTime(String sellTime) {
		this.sellTime = sellTime;
	}
	public String getLastPayTime() {
		return lastPayTime;
	}
	public void setLastPayTime(String lastPayTime) {
		this.lastPayTime = lastPayTime;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getBuyerDate() {
		return buyerDate;
	}
	public void setBuyerDate(String buyerDate) {
		this.buyerDate = buyerDate;
	}

}
