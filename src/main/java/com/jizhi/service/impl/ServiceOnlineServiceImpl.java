package com.jizhi.service.impl;

import com.jizhi.dao.ServiceOnlineDao;
import com.jizhi.dao.UserDao;
import com.jizhi.pojo.ServiceOnline;
import com.jizhi.pojo.User;
import com.jizhi.service.ServiceOnlineService;
import com.jizhi.util.Base64ToImgUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Transactional(rollbackFor = Exception.class)
@Service
public class ServiceOnlineServiceImpl implements ServiceOnlineService{
	
	@Autowired
	private ServiceOnlineDao serviceOnlineDao;
	
	@Autowired
	private Base64ToImgUtil base64ToImgUtil;
	
	@Autowired
	private UserDao userDao;

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
	
	/**
	 * 显示有消息未得到回复的用户列表
	 */
	@Override
	public List<User> queryNotAnswered() {
		List<ServiceOnline> list=serviceOnlineDao.queryNotAnswered();
		ArrayList<User> users = new ArrayList<User>();
		if(list.size()>0) {
			for(ServiceOnline serviceOnline:list) {
				User user = userDao.queryById(serviceOnline.getUserId());
				users.add(user);
			}	
		}
		return users;
	}

	@Override
	public int addAnswer(ServiceOnline serviceOnline) {
		//添加回复
		serviceOnline.setIsAnswered(1);
		int i=serviceOnlineDao.addAnswer(serviceOnline);
		if(i>0) {
			//回复成功后更改改用户之前的消息都为已回复
			return serviceOnlineDao.updateIsAnswered(serviceOnline.getUserId());
		}
		return 0;
	}

}
