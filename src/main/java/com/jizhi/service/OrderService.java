package com.jizhi.service;

import java.util.HashMap;
import java.util.List;

import com.jizhi.pojo.Order;
import com.jizhi.pojo.vo.OrderDetail;

public interface OrderService{

	int addOrder(Order order);

	List<Integer> toOrder(Integer animalId, String token);

	List<Order> queryAll(HashMap<String, Object> map);

	void updateState(Integer orderId);

	List<OrderDetail> queryAllByUserId(String token);
	
	List<Order> querySuccessOrder(Integer buyerId);

}
