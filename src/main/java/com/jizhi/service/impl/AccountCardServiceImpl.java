package com.jizhi.service.impl;

import com.jizhi.dao.AccountCardDao;
import com.jizhi.pojo.AccountCard;
import com.jizhi.service.AccountCardService;
import com.jizhi.util.Base64ToImgUtil;
import com.jizhi.util.RedisService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional(rollbackFor = Exception.class)
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
		String pic;
		//将前端传来的图片上传到本地并记录地址pic
		if(StringUtils.isEmpty(accountCard.getPic())) {
			return 0;
		}else {
			pic= base64ToImgUtil.base64(accountCard.getPic());
		}
		//根据token得到userId
		String userId = this.redisService.get(token);
		accountCard.setUserId(Integer.valueOf(userId));
		if(StringUtils.isEmpty(accountCard.getAccountNum()) 
				|| StringUtils.isEmpty(accountCard.getType())) {
			return 0;
		}else {
			accountCard.setAccountName(null);
			accountCard.setPic(pic);
			return this.accountCardDao.save(accountCard);
		}
	}
	@Override
	public int delIdCard(Integer id) {
		int i=this.accountCardDao.del(id);
		return i;
	}
	@Override
	public int updateCard(AccountCard accountCard,String token) {
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
