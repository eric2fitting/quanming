package com.jizhi.service.impl;

import com.jizhi.dao.NormalQueDao;
import com.jizhi.pojo.NormalQue;
import com.jizhi.service.NormalQueService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional(rollbackFor = Exception.class)
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
	
	@Override
	public int delete(Integer id) {
		return normalQueDao.delete(id);
	}
	@Override
	public int update(NormalQue normalQue) {
		if(StringUtils.isEmpty(normalQue.getTitle()) 
				|| StringUtils.isEmpty(normalQue.getContent())) {
			return 0;
		}else {
			return normalQueDao.update(normalQue);
		}
		
	}

}
