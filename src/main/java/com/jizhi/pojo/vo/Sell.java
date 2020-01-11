package com.jizhi.pojo.vo;

import java.io.Serializable;

public class Sell implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer id;//匹配表id
	private String secondPsw;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSecondPsw() {
		return secondPsw;
	}
	public void setSecondPsw(String secondPsw) {
		this.secondPsw = secondPsw;
	}
	
}
