package com.jizhi.service;

import com.jizhi.pojo.Profits;
import com.jizhi.pojo.vo.UserInfo;

public interface ProfitsService {

	Profits selectAllProfits(Integer id);

	void add(Profits profits);
	//查找用户为邀请者创造的分享收益
	Double queryAllShareProfitToOthers(Profits profits);
	
	
	Integer updateShareProfit(String token, UserInfo userInfo);
	//查找用户自己得到的总分享收益
	Double queryShareProfit(Integer id);
	//根据用户id查询总的动物收益
	Double queryAllAnimalProfits(Integer id);
	//根据用户id查询总的NFC币
	Integer queryAllNFC(Integer id);




}
