package com.jizhi.service.impl;

import com.jizhi.dao.FirstPageDao;
import com.jizhi.pojo.FirstPage;
import com.jizhi.pojo.Message;
import com.jizhi.service.FirstPageService;
import com.jizhi.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
@Transactional(rollbackFor = Exception.class)
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
		if(firstPage!=null) {
			map.put("banner",firstPage.getBannerPic());
			map.put("announcement", firstPage.getAnnouncement());
		}else {
			map.put("banner",null);
			map.put("announcement", null);
		}
		if(message!=null) {
			map.put("title",message.getTitle());
		}else {
			map.put("title",null);
		}
		return map;
	}
}
