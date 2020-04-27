package com.jizhi.pojo.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;


public class ShareProfitsVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Double shareProfit;
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date date;
	private String result;
	public Double getShareProfit() {
		return shareProfit;
	}
	public void setShareProfit(Double shareProfit) {
		this.shareProfit = shareProfit;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
}
