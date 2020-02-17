package com.jizhi.pojo.vo;

import java.io.Serializable;

public class UserInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;
	private String tel;
	private Double totalMoney;
	private Double animalProfit;
	private Double shareProfit;
	private Integer NFC;
	private Integer level;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Double getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
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
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}

	
	
}
