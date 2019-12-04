package com.jizhi.pojo;
/**
 * 资产表
 * @author Administrator
 *
 */
public class Property {
	private Integer id;
	private Integer userId;
	private Integer animalId;
	private Double price;
	private String buyDate;
	private String buyTime;
	private Integer isSold;//0表示没有，1正在出售，2表示已经出售
	
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
	public Integer getAnimalId() {
		return animalId;
	}
	public void setAnimalId(Integer animalId) {
		this.animalId = animalId;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getBuyDate() {
		return buyDate;
	}
	public void setBuyDate(String buyDate) {
		this.buyDate = buyDate;
	}
	public String getBuyTime() {
		return buyTime;
	}
	public void setBuyTime(String buyTime) {
		this.buyTime = buyTime;
	}
	public Integer getIsSold() {
		return isSold;
	}
	public void setIsSold(Integer isSold) {
		this.isSold = isSold;
	}

}
