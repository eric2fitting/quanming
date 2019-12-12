package com.jizhi.service.impl;

import org.apache.commons.lang.StringUtils;
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
	/**
	 * 保存身份证 
	 */
	public int saveIdCard(IdCard idCard,String token) {
		//将前端传来的图片上传到本地并记录地址pic
		String pic = base64ToImgUtil.base64(idCard.getPic());
		
		if(StringUtils.isEmpty(pic) || StringUtils.isEmpty(idCard.getIdNum()) 
				|| StringUtils.isEmpty(idCard.getName())) {
			return 0;
		}else {
			idCard.setPic(pic);
			//根据token得到userId
			String userId = this.redisService.get(token);
			idCard.setUserId(Integer.valueOf(userId));
			return this.IdCardDao.save(idCard);
		}

	}

	@Override
	public int updateIdCard(IdCard idCard,String token) {
		//将前端传来的图片上传到本地并记录地址pic
		String pic = base64ToImgUtil.base64(idCard.getPic());
		if(StringUtils.isEmpty(pic) || StringUtils.isEmpty(idCard.getIdNum()) 
				|| StringUtils.isEmpty(idCard.getName())) {
			return 0;
		}else {
			idCard.setPic(pic);
			//根据token得到userId
			String userId = this.redisService.get(token);
			idCard.setUserId(Integer.valueOf(userId));
			return this.IdCardDao.update(idCard);
		}
		
	}

}
