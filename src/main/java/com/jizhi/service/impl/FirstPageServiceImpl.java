package com.jizhi.service.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jizhi.dao.FirstPageDao;
import com.jizhi.pojo.FirstPage;
import com.jizhi.pojo.Message;
import com.jizhi.service.FirstPageService;
import com.jizhi.service.MessageService;

@Service
public class FirstPageServiceImpl implements FirstPageService{
	
	@Autowired
	private FirstPageDao firstPageDao;
	@Autowired
	private MessageService messageService;
	@Override
	public HashMap<String, Object> queryfirstPageInfo() {
		FirstPage firstPage=firstPageDao.query();
		Message message = messageService.queryLatestMsg();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("banner",firstPage.getBannerPic());
		map.put("announcement", firstPage.getAnnouncement());
		map.put("title",message.getTitle());
		return map;
	}

}
