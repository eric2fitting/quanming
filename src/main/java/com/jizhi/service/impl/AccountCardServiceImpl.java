package com.jizhi.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jizhi.dao.AccountCardDao;
import com.jizhi.pojo.AccountCard;
import com.jizhi.service.AccountCardService;
import com.jizhi.util.Base64ToImgUtil;
import com.jizhi.util.RedisService;

@Service
public class AccountCardServiceImpl implements AccountCardService{
	
	@Autowired
	private AccountCardDao accountCardDao;
	@Autowired
	private RedisService redisService;
	@Autowired
	private Base64ToImgUtil base64ToImgUtil;

	
	@Override
	public int save(AccountCard accountCard,String token) {
		//将前端传来的图片上传到本地并记录地址pic
		String pic = base64ToImgUtil.base64(accountCard.getPic());
		//根据token得到userId
		String userId = this.redisService.get(token);
		accountCard.setUserId(Integer.valueOf(userId));
		if(StringUtils.isEmpty(accountCard.getAccountName()) 
				|| StringUtils.isEmpty(accountCard.getAccountNum()) 
				|| StringUtils.isEmpty(accountCard.getType())) {
			return 0;
		}else {
			accountCard.setPic(pic);
			if(accountCard.getType().equals("支付宝") || accountCard.getType().equals("微信")) {
				if(StringUtils.isEmpty(pic)) {
					return 0;
				}
			}
			return this.accountCardDao.save(accountCard);
		}
		
	}
	@Override
	public int delIdCard(String token) {
		Integer userId=Integer.parseInt(this.redisService.get(token));
		int i=this.accountCardDao.del(userId);
		return i;
	}
	@Override
	public int updateIdCard(AccountCard accountCard,String token) {
		//将前端传来的图片上传到本地并记录地址pic
		String pic = base64ToImgUtil.base64(accountCard.getPic());
		accountCard.setPic(pic);
		//根据token得到userId
		String userId = this.redisService.get(token);
		accountCard.setUserId(Integer.valueOf(userId));
		return this.accountCardDao.update(accountCard);
	}
	@Override
	public List<AccountCard> queryAll(String token) {
		String string = redisService.get(token);
		Integer userId = Integer.parseInt(string);
		return accountCardDao.queryAll(userId);
	}
	
	
}
