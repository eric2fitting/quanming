package com.jizhi.pojo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Feed {
	
	private Integer id;
	private Integer userId;
	private Double num;
	private Integer type;//1预约扣除、2系统发放、3转入、4兑换、5转出
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date date;
	
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
	public Double getNum() {
		return num;
	}
	public void setNum(Double num) {
		this.num = num;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
}
