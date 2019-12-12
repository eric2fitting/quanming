package com.jizhi.service;

import java.util.HashMap;
import java.util.List;

import com.jizhi.pojo.Order;
import com.jizhi.pojo.vo.AnimaInfo;
import com.jizhi.pojo.vo.OrderDetail;

public interface OrderService{

	int addOrder(Integer id,String token);

	AnimaInfo toOrder(Integer animalId, String token);

	List<Order> queryAll(HashMap<String, Object> map);

	void updateState(Integer orderId);

	List<OrderDetail> queryAllByUserId(String token);
	
	List<Order> querySuccessOrder(Integer buyerId);

	Order queryByOrderId(Integer orderId);
	

}
