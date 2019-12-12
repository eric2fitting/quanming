package com.jizhi.dao;

import org.apache.ibatis.annotations.Mapper;

import com.jizhi.pojo.Profits;

@Mapper
public interface ProfitsDao {

	Profits selectAllProfits(int userId);

	void save(Profits profits);

	Double queryAllShareProfit(Integer id);

}
