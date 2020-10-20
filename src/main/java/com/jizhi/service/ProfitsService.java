package com.jizhi.service;

import java.util.List;

import com.jizhi.pojo.Profits;
import com.jizhi.pojo.vo.FeedExchangeParam;
import com.jizhi.pojo.vo.ShareProfitsVO;
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
	Double queryAllNFC(Integer id);

	FeedExchangeParam tryShareProfitsToFeed(Integer userId);

	Integer shareProfitsToFeed(FeedExchangeParam feedExchangeParam);

//	FeedExchangeParam tryNFCToFeed(Integer userId);
//
//	Integer NFCToFeed(FeedExchangeParam feedExchangeParam);

	Double getAllShare(Integer sharerId);
	//提现列表
	List<ShareProfitsVO> queryShareProfitsList(String token);


}
