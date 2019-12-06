package com.jizhi.service;

import java.util.HashMap;
import java.util.List;

import com.jizhi.pojo.Match;
import com.jizhi.pojo.Order;
import com.jizhi.pojo.Property;
import com.jizhi.pojo.vo.BuyingDetail;
import com.jizhi.pojo.vo.FeedingDetail;

public interface MatchService {
	//增加匹配信息
	Integer add(Match match);
	//进行匹配
	void doMatch(Order order, Property property);
	List<BuyingDetail> showAllBuying(String token);
	Match queryByOrderId(Integer id);
	List<FeedingDetail> showAllFeeding(String token);
	//根据资产id和买卖双方是否确认查找匹配信息
	Match queryByPropertyId(HashMap<String, Object> map);
}
