package com.jizhi.pojo.vo;

import java.io.Serializable;

public class PswInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String oldPsw;
	private String newPsw;
	public String getOldPsw() {
		return oldPsw;
	}
	public void setOldPsw(String oldPsw) {
		this.oldPsw = oldPsw;
	}
	public String getNewPsw() {
		return newPsw;
	}
	public void setNewPsw(String newPsw) {
		this.newPsw = newPsw;
	}
	
}
