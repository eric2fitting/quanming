package com.jizhi.pojo;
/**
 * 动物类
 */
import java.io.Serializable;

public class Animal implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String animalType;
	private String size;
	private double minPrice;
	private double maxPrice;
	private Integer profit;
	private Integer cycle;
	private Integer nfc;
	private Integer isShow;
	
	public Integer getIsShow() {
		return isShow;
	}
	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public double getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}
	public double getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}
	public Integer getProfit() {
		return profit;
	}
	public void setProfit(Integer profit) {
		this.profit = profit;
	}
	public Integer getCycle() {
		return cycle;
	}
	public void setCycle(Integer cycle) {
		this.cycle = cycle;
	}
	public Integer getNfc() {
		return nfc;
	}
	public void setNfc(Integer nfc) {
		this.nfc = nfc;
	}
	
}
