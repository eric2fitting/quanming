package com.jizhi.service.impl;

import javax.servlet.http.HttpServletRequest;

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
	public int save(AccountCard accountCard, HttpServletRequest request,String token) {
		//将前端传来的图片上传到本地并记录地址pic
		String pic = base64ToImgUtil.Base64ToImg(accountCard.getPic(), request);
		accountCard.setPic(pic);
		//根据token得到userId
		String userId = this.redisService.get(token);
		accountCard.setUserId(Integer.valueOf(userId));
		return this.accountCardDao.save(accountCard);
	}
	@Override
	public int delIdCard(String token) {
		Integer userId=Integer.parseInt(this.redisService.get(token));
		int i=this.accountCardDao.del(userId);
		return i;
	}
	@Override
	public int updateIdCard(AccountCard accountCard, HttpServletRequest request,String token) {
		//将前端传来的图片上传到本地并记录地址pic
		String pic = base64ToImgUtil.Base64ToImg(accountCard.getPic(), request);
		accountCard.setPic(pic);
		//根据token得到userId
		String userId = this.redisService.get(token);
		accountCard.setUserId(Integer.valueOf(userId));
		return this.accountCardDao.update(accountCard);
	}
	
	
}
