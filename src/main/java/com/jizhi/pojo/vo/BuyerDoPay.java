package com.jizhi.pojo.vo;

import java.io.Serializable;

public class BuyerDoPay implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer matchId;
	private String payPic;
	private String secondPsw;
	public String getPayPic() {
		return payPic;
	}
	public void setPayPic(String payPic) {
		this.payPic = payPic;
	}
	public String getSecondPsw() {
		return secondPsw;
	}
	public void setSecondPsw(String secondPsw) {
		this.secondPsw = secondPsw;
	}
	public Integer getMatchId() {
		return matchId;
	}
	public void setMatchId(Integer matchId) {
		this.matchId = matchId;
	}
	
}
