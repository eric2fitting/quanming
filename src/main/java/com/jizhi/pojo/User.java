package com.jizhi.pojo;

import java.io.Serializable;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String userName;
	private String tel;
	private String state;
	private Integer isFrozen;
	private String password;
	private String inviteCode;
	private String invitedCode;
	private Integer isConfirmed;
	private String secondpsw;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSecondpsw() {
		return secondpsw;
	}
	public void setSecondpsw(String secondpsw) {
		this.secondpsw = secondpsw;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Integer getIsFrozen() {
		return isFrozen;
	}
	public void setIsFrozen(Integer isFrozen) {
		this.isFrozen = isFrozen;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getInviteCode() {
		return inviteCode;
	}
	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}
	public String getInvitedCode() {
		return invitedCode;
	}
	public void setInvitedCode(String invitedCode) {
		this.invitedCode = invitedCode;
	}
	public Integer getIsConfirmed() {
		return isConfirmed;
	}
	public void setIsConfirmed(Integer isConfirmed) {
		this.isConfirmed = isConfirmed;
	}

	
}
