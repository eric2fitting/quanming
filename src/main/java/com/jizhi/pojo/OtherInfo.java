package com.jizhi.pojo;

import java.io.Serializable;

public class OtherInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String qq;//qq号码
	private String weChat;//微信号码
	private Double earnMoney;//提现金额
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getWeChat() {
		return weChat;
	}
	public void setWeChat(String weChat) {
		this.weChat = weChat;
	}
	public Double getEarnMoney() {
		return earnMoney;
	}
	public void setEarnMoney(Double earnMoney) {
		this.earnMoney = earnMoney;
	}
	
}
