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
	private Integer isConfirmed;//0表示没上传 ，1表示确定，2审核中，3未通过。
	private String secondpsw;
	private Integer role;
	private String cid;
	private Integer level;//会员等级0普通会员，1:V1,2:V2,3:V3,4:V4
	public Integer getRole() {
		return role;
	}
	public void setRole(Integer role) {
		this.role = role;
	}

	
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
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}

	
}
