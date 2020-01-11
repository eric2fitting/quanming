package com.jizhi.service;

import com.jizhi.pojo.AccountCard;

import java.util.List;

public interface AccountCardService {
	/**
	 * 新增账户卡
	 * @param accountCard
	 * @param token
	 * @return
	 */
	int save(AccountCard accountCard,String token);
	
	/**
	 * 删除账户卡
	 * @param token
	 * @return
	 */
	int delIdCard(Integer id);

	/**
	 * 更新账户卡
	 * @param accountCard
	 * @param token
	 * @return
	 */
	int updateCard(AccountCard accountCard,String token);
	
	//银行卡或支付宝微信等收款方式列表
	List<AccountCard> queryAll(String token);
	
	

}
