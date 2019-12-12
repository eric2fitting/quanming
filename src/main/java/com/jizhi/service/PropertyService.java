package com.jizhi.service;

import java.util.HashMap;
import java.util.List;

import com.jizhi.pojo.Property;
import com.jizhi.pojo.vo.FeedingDetail;
import com.jizhi.pojo.vo.SellInfo;

public interface PropertyService {
	//查询可售的动物
	List<Property> queryCanSell(HashMap<String,Object> map);
	//根据id更改状态
	//void updateState(Property property);
	//根据userId查询所有资产
	Double queryTotalMonet(Integer id);
	//根据主键id查询该条数据
	Property queryById(Integer id);
	//根据userId查询所有拥有的动物
	List<Property> queryByUserId(Integer userId);
	//查询正在等待转让的动物列表
	List<FeedingDetail> queryWaitSellList(String token);
	//查询正在转让的动物列表
	List<FeedingDetail> queryIsSellingList(String token);
	//查询已经转让的动物列表
	List<FeedingDetail> queryIsSoldList(String token);
	//确认转让页面
	SellInfo toSell(Integer matchId);
	//确认转让
	void doSell(Integer matchId);
	//添加
	void add(Property property);
	
	void updateToSold(Integer id);
	
	void updateState(Property property);

}
