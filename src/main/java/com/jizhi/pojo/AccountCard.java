package com.jizhi.pojo;

import java.io.Serializable;

/**
 * 银行卡信息类
 * @author Administrator
 *
 */
public class AccountCard implements Serializable{
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer userId;
	private String type;//
	private String accountName;//现在没用了不管
	private String accountNum;//账户名
	private String pic;//照片
	

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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountNum() {
		return accountNum;
	}
	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
