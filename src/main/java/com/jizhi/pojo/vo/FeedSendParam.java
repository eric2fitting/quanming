package com.jizhi.pojo.vo;

import java.io.Serializable;

public class FeedSendParam implements Serializable{

	private static final long serialVersionUID = -4712296126863998425L;
	private Integer UserId;
	private String tel;
	private String seconPsw;
	private Double num;
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getSeconPsw() {
		return seconPsw;
	}
	public void setSeconPsw(String seconPsw) {
		this.seconPsw = seconPsw;
	}
	public Double getNum() {
		return num;
	}
	public void setNum(Double num) {
		this.num = num;
	}
	public Integer getUserId() {
		return UserId;
	}
	public void setUserId(Integer userId) {
		UserId = userId;
	}
	
}
