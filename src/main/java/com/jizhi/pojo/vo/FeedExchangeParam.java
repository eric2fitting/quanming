package com.jizhi.pojo.vo;
/**
 * 用于NFC和分享收益兑换饲料
 * @author Administrator
 *
 */
import java.io.Serializable;



public class FeedExchangeParam implements Serializable{

	private static final long serialVersionUID = -2943457051035759663L;
	private Integer userId;
	private Integer level;//用户等级
	private Integer maxExchangeNum;//最大可兑换的数量
	private Double shareProfits;//全部可用分享收益
	private Integer NFC;//NFC币
	private Integer exchangeNum;//兑换的数量;
	private String secondPsw;//二级密码
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getMaxExchangeNum() {
		return maxExchangeNum;
	}
	public void setMaxExchangeNum(Integer maxExchangeNum) {
		this.maxExchangeNum = maxExchangeNum;
	}
	public Double getShareProfits() {
		return shareProfits;
	}
	public void setShareProfits(Double shareProfits) {
		this.shareProfits = shareProfits;
	}
	public Integer getExchangeNum() {
		return exchangeNum;
	}
	public void setExchangeNum(Integer exchangeNum) {
		this.exchangeNum = exchangeNum;
	}
	public String getSecondPsw() {
		return secondPsw;
	}
	public void setSecondPsw(String secondPsw) {
		this.secondPsw = secondPsw;
	}
	public Integer getNFC() {
		return NFC;
	}
	public void setNFC(Integer nFC) {
		NFC = nFC;
	}
	
}
