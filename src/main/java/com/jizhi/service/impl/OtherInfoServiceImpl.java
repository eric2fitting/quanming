package com.jizhi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jizhi.dao.OtherInfoDao;
import com.jizhi.pojo.OtherInfo;
import com.jizhi.service.OtherInfoService;

@Transactional(rollbackFor = Exception.class)
@Service
public class OtherInfoServiceImpl implements OtherInfoService{
	
	
	@Autowired
	private OtherInfoDao otherInfoDao;
	@Override
	public OtherInfo query() {
		return otherInfoDao.query();
	}

}
