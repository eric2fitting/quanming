package com.jizhi.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jizhi.pojo.Order;

@Mapper
public interface OrderDao {

	int save(Order order);


	Order queryByUserIdAndTime(Order order);

	
	List<Order> queryAll(HashMap<String, Object> map);


	void updateState(Integer id);


	List<Order> queryAllByUserId(Integer userId);


	List<Order> querySuccessOrder(Integer userId);


	Order queryById(Integer id);


	Integer insert(Order order);


	void updateToFail(Order order);


	void deleteAll();


	List<Order> queryFailedOrder(Order order);




}
