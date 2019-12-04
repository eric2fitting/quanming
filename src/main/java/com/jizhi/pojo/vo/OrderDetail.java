package com.jizhi.pojo.vo;

import java.io.Serializable;

public class OrderDetail implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String date;
	private String animalType;
	private String size;
	private Integer state;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
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
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	
}
