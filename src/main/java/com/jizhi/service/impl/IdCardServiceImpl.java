package com.jizhi.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jizhi.dao.IdCardDao;
import com.jizhi.dao.UserDao;
import com.jizhi.pojo.IdCard;
import com.jizhi.service.IdCardService;
import com.jizhi.service.UserSevice;
import com.jizhi.util.Base64ToImgUtil;
import com.jizhi.util.RedisService;
@Transactional(rollbackFor = Exception.class)
@Service
public class IdCardServiceImpl implements IdCardService{
	@Autowired
	private IdCardDao IdCardDao;
	@Autowired
	private Base64ToImgUtil base64ToImgUtil;
	@Autowired
	private RedisService redisService;
	@Autowired
	private UserSevice userSevice;
	@Autowired
	private UserDao userDao;
	@Override
	/**
	 * 保存身份证 
	 */
	public int saveIdCard(IdCard idCard,String token) {
		//将前端传来的图片上传到本地并记录地址pic
		String pic = base64ToImgUtil.base64(idCard.getPic());
		int i;
		if(StringUtils.isEmpty(pic) || StringUtils.isEmpty(idCard.getIdNum()) 
				|| StringUtils.isEmpty(idCard.getName())
				|| (idCard.getIdNum().length()!=15 && idCard.getIdNum().length()!=18)) {
			i= 0;
		}else {
			idCard.setPic(pic);
			//根据token得到userId
			String user_Id = this.redisService.get(token);
			Integer userId=Integer.valueOf(user_Id);
			idCard.setUserId(userId);
			i=this.IdCardDao.save(idCard);
			//上传成功后，将该用户设置为审核中
			if(i>0) {
				userSevice.updateIsConfirmed(userId);
			}
		}
		return i;
	}

	@Override
	public int updateIdCard(IdCard idCard,String token) {
		//将前端传来的图片上传到本地并记录地址pic
		String pic=idCard.getPic();
		if(StringUtils.isNotEmpty(pic)){
			pic = base64ToImgUtil.base64(pic);
		}
		int i;
		if(StringUtils.isEmpty(pic) || StringUtils.isEmpty(idCard.getIdNum()) 
				|| StringUtils.isEmpty(idCard.getName())) {
			i=0;
		}else {
			idCard.setPic(pic);
			//根据token得到userId
			String user_Id = this.redisService.get(token);
			Integer userId=Integer.valueOf(user_Id);
			idCard.setUserId(userId);
			i=this.IdCardDao.update(idCard);
			if(i>0) {
				//将该用户设置为审核中
				userSevice.updateIsConfirmed(userId);
			}
		}
		return i;
	}
	
	/**
	 * 查询该用户的身份认证状态
	 */
	@Override
	public Integer queryIsConfirmed(String token) {
		Integer id = Integer.parseInt(redisService.get(token));
		return userDao.queryById(id).getIsConfirmed();
	}

}
