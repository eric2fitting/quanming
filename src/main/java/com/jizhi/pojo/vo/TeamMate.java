package com.jizhi.pojo.vo;

import java.io.Serializable;

public class TeamMate implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private Double shareMoney;
	private String state;
	private Integer level;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getShareMoney() {
		return shareMoney;
	}
	public void setShareMoney(Double shareMoney) {
		this.shareMoney = shareMoney;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	
}
