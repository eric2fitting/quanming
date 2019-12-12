package com.jizhi.pojo;
/**
 * 买卖双方匹配对象
 * @author Administrator
 *
 */
public class Match {
	private Integer id;
	private Integer orderId;
	private Integer propertyId;
	private Integer buyerConfirm;
	private Integer sellerConfirm;
	private Double price;
	private String payPic;
	
	
	public String getPayPic() {
		return payPic;
	}
	public void setPayPic(String payPic) {
		this.payPic = payPic;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getPropertyId() {
		return propertyId;
	}
	public void setPropertyId(Integer propertyId) {
		this.propertyId = propertyId;
	}
	public Integer getBuyerConfirm() {
		return buyerConfirm;
	}
	public void setBuyerConfirm(Integer buyerConfirm) {
		this.buyerConfirm = buyerConfirm;
	}
	public Integer getSellerConfirm() {
		return sellerConfirm;
	}
	public void setSellerConfirm(Integer sellerConfirm) {
		this.sellerConfirm = sellerConfirm;
	}

	
	
}
