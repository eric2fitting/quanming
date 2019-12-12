package com.jizhi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jizhi.dao.ProfitsDao;
import com.jizhi.pojo.Profits;
import com.jizhi.service.ProfitsService;

@Service
public class ProfitsServiceImpl implements ProfitsService{
	
	@Autowired
	private ProfitsDao profitsDao;
	
	/**
	 * 根据用户id查询他各部分的总收益
	 */
	@Override
	public Profits selectAllProfits(int id) {
		return profitsDao.selectAllProfits(id);
	}

	@Override
	public void add(Profits profits) {
		profitsDao.save(profits);
		
	}

	@Override
	public Double queryAllShareProfit(Integer id) {
		return profitsDao.queryAllShareProfit(id);
	}

}
