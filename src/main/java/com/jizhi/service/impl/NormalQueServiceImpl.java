package com.jizhi.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jizhi.dao.NormalQueDao;
import com.jizhi.pojo.NormalQue;
import com.jizhi.service.NormalQueService;

@Service
public class NormalQueServiceImpl implements NormalQueService{
	@Autowired
	private NormalQueDao normalQueDao;
	@Override
	public Integer add(NormalQue normalQue) {
		Integer integer;
		if(StringUtils.isEmpty(normalQue.getTitle()) 
				|| StringUtils.isEmpty(normalQue.getContent())) {
			integer=0;
		}else {
			integer=normalQueDao.add(normalQue);
		}
		return integer;
	}
	@Override
	public List<NormalQue> queryAll() {
		return normalQueDao.queryAll();
	}

}
