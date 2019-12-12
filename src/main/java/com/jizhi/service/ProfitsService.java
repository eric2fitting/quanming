package com.jizhi.service;

import com.jizhi.pojo.Profits;

public interface ProfitsService {

	Profits selectAllProfits(int id);

	void add(Profits profits);

	Double queryAllShareProfit(Integer id);



}
