package com.jizhi.service;

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
	
	

}
