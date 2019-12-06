package com.jizhi.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.jizhi.pojo.AccountCard;

public interface AccountCardService {
	/**
	 * 新增账户卡
	 * @param accountCard
	 * @param token
	 * @return
	 */
	int save(AccountCard accountCard, HttpServletRequest request,String token);
	
	/**
	 * 删除账户卡
	 * @param token
	 * @return
	 */
	int delIdCard(String token);

	/**
	 * 更新账户卡
	 * @param accountCard
	 * @param token
	 * @return
	 */
	int updateIdCard(AccountCard accountCard, HttpServletRequest request,String token);
	
	//银行卡或支付宝微信等收款方式列表
	List<AccountCard> queryAll(String token);
	
	

}
