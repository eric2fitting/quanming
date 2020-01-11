package com.jizhi.pojo.vo;

import java.io.Serializable;

/**
 * 
 * @author Administrator
 *
 */
public class FeedingDetail implements Serializable {
	private static final long serialVersionUID = 1L;
	private String number;//区块编号
	private Integer id;//订单id
	private String buyTime;//领养时间：日期+时间
	private String animalType;//动物类型
	private String size;//动物大小
	private String price;//价格：显示买家×（100+10）%
	private String cycleProfit;//周期收益，几天涨百分之几十+卖价
	private String profit;
	private String sellTime;//多久后卖：日期+时间
	private String state;
	private String noteMsg;
	private String LastTime;//最后确认时间
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBuyTime() {
		return buyTime;
	}
	public void setBuyTime(String buyTime) {
		this.buyTime = buyTime;
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
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getCycleProfit() {
		return cycleProfit;
	}
	public void setCycleProfit(String cycleProfit) {
		this.cycleProfit = cycleProfit;
	}
	public String getProfit() {
		return profit;
	}
	public void setProfit(String profit) {
		this.profit = profit;
	}
	public String getSellTime() {
		return sellTime;
	}
	public void setSellTime(String sellTime) {
		this.sellTime = sellTime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getNoteMsg() {
		return noteMsg;
	}
	public void setNoteMsg(String noteMsg) {
		this.noteMsg = noteMsg;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getLastTime() {
		return LastTime;
	}
	public void setLastTime(String lastTime) {
		LastTime = lastTime;
	}
	
}
