package com.jizhi.pojo.vo;

import com.jizhi.pojo.AccountCard;

import java.io.Serializable;
import java.util.List;

public class PayInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;//姓名
	private String tel;//电话
	private Double price;//价格
	private Double usdtPrice;//火币网价格
	private List<AccountCard> accountCardList;//付款方式
	private Integer isConfirm;//是否付款0没哟u，1付款了。
	private String payPic;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public List<AccountCard> getAccountCardList() {
		return accountCardList;
	}
	public void setAccountCardList(List<AccountCard> accountCardList) {
		this.accountCardList = accountCardList;
	}
	public Integer getIsConfirm() {
		return isConfirm;
	}
	public void setIsConfirm(Integer isConfirm) {
		this.isConfirm = isConfirm;
	}
	public String getPayPic() {
		return payPic;
	}
	public void setPayPic(String payPic) {
		this.payPic = payPic;
	}
	public Double getUsdtPrice() {
		return usdtPrice;
	}
	public void setUsdtPrice(Double usdtPrice) {
		this.usdtPrice = usdtPrice;
	}
	
	
}
