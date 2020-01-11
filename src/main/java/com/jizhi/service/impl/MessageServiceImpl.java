package com.jizhi.service.impl;

import com.jizhi.dao.MessageDao;
import com.jizhi.pojo.Message;
import com.jizhi.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@Transactional(rollbackFor = Exception.class)
@Service
public class MessageServiceImpl implements MessageService{
	@Autowired
	private MessageDao messageDao;
			
	@Override
	public List<Message> queryAll() {
		
		return messageDao.queryAll();
	}
	@Override
	public List<Message> queryByType(Integer type) {
		return messageDao.queryByType(type);
	}

	@Override
	public Integer save(Message message) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		message.setTime(simpleDateFormat.format(date));
		return messageDao.add(message);
	}
	
	//查询最新消息
	@Override
	public Message queryLatestMsg() {
		return messageDao.queryLatestMsg();
	}
	@Override
	public Integer delete(Integer id) {
		if(id!=null) {
			return messageDao.delete(id);
		}
		return null;
	}


} 
