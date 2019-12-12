package com.jizhi.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jizhi.dao.MessageDao;
import com.jizhi.pojo.Message;
import com.jizhi.service.MessageService;

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


} 
