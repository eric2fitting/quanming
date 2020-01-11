package com.jizhi.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.jizhi.pojo.Match;
import com.jizhi.pojo.Order;
import com.jizhi.pojo.Property;
import com.jizhi.pojo.vo.BuyerDoPay;
import com.jizhi.pojo.vo.BuyingDetail;
import com.jizhi.pojo.vo.FeedingDetail;
import com.jizhi.pojo.vo.PayInfo;

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
	//付款页面
	PayInfo buyerToPay(Integer matchId);
	//买家确认付款
	String doPay(BuyerDoPay buyerDoPay, HttpServletRequest request);
	//查询所有买家还未付款匹配的信息
	List<Match> queryAllByBuyerConfirm();
	//查询所有卖家还未付款匹配的信息（买家付款了）
	List<Match> queryAllBySellerConfirm();
	//根据id更改买家确认状态
	void updateSellerConfirm(Integer id);
	//删除匹配信息。
	void deleteById(Integer id);
	Integer cancelSell(Integer id);
}
