package com.jizhi.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jizhi.dao.ServiceOnlineDao;
import com.jizhi.pojo.ServiceOnline;
import com.jizhi.service.ServiceOnlineService;
import com.jizhi.util.Base64ToImgUtil;

@Service
public class ServiceOnlineServiceImpl implements ServiceOnlineService{
	
	@Autowired
	private ServiceOnlineDao serviceOnlineDao;
	
	@Autowired
	private Base64ToImgUtil base64ToImgUtil;

	@Override
	public List<ServiceOnline> queryByUserId(Integer userId) {
		return serviceOnlineDao.queryByUserId(userId);
	}
	
	/**
	 * 发送文字问题
	 */
	@Override
	public int addMsg(ServiceOnline serviceOnline) {
		serviceOnline.setIsAnswered(0);
		return serviceOnlineDao.addMsg(serviceOnline);
	}
	
	/**
	 * 在线客服问题之图片
	 */
	@Override
	public int addPic(ServiceOnline serviceOnline) {
		String base64 = serviceOnline.getPic();
		int i;
		if(StringUtils.isEmpty(base64)) {
			i= 0;
		}else {
			String pic = base64ToImgUtil.base64(base64);
			serviceOnline.setPic(pic);
			serviceOnline.setIsAnswered(0);
			i=serviceOnlineDao.addPic(serviceOnline);
		}
		return i;
		
	}

}
