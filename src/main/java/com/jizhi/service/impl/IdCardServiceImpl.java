package com.jizhi.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jizhi.dao.IdCardDao;
import com.jizhi.pojo.IdCard;
import com.jizhi.service.IdCardService;
import com.jizhi.util.Base64ToImgUtil;
import com.jizhi.util.RedisService;

@Service
public class IdCardServiceImpl implements IdCardService{
	@Autowired
	private IdCardDao IdCardDao;
	@Autowired
	private Base64ToImgUtil base64ToImgUtil;
	@Autowired
	private RedisService redisService;
	@Override
	public int saveIdCard(IdCard idCard,HttpServletRequest request,String token) {
		//将前端传来的图片上传到本地并记录地址pic
		String pic = base64ToImgUtil.Base64ToImg(idCard.getPic(), request);
		idCard.setPic(pic);
		//根据token得到userId
		String userId = this.redisService.get(token);
		idCard.setUserId(Integer.valueOf(userId));
		return this.IdCardDao.save(idCard);
	}

	@Override
	public int updateIdCard(IdCard idCard,HttpServletRequest request,String token) {
		//将前端传来的图片上传到本地并记录地址pic
		String pic = base64ToImgUtil.Base64ToImg(idCard.getPic(), request);
		idCard.setPic(pic);
		//根据token得到userId
		String userId = this.redisService.get(token);
		idCard.setUserId(Integer.valueOf(userId));
		return this.IdCardDao.update(idCard);
	}

}
