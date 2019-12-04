package com.jizhi.pojo.vo;

import java.io.Serializable;

public class LoginInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tel;//电话号码
	private String password;//密码
	private String secondPassword;//二级密码
	private String code;//短信验证码
	private String invitedCode;
	public String getInvitedCode() {
		return invitedCode;
	}
	public void setInvitedCode(String invitedCode) {
		this.invitedCode = invitedCode;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSecondPassword() {
		return secondPassword;
	}
	public void setSecondPassword(String secondPassword) {
		this.secondPassword = secondPassword;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
