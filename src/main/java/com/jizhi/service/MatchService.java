package com.jizhi.service;

import java.util.List;

import com.jizhi.pojo.Match;
import com.jizhi.pojo.Order;
import com.jizhi.pojo.Property;
import com.jizhi.pojo.vo.BuyingDetail;

public interface MatchService {
	Integer add(Match match);
	void doMatch(Order order, Property property);
	List<BuyingDetail> showAllBuying(String token);
	Match queryByOrderId(Integer id);
}
