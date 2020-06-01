package com.jizhi.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

	@Select("select id,userId ,animalId from orders where animalId=#{animalId} and time=#{time} and state=1 and flag=0")
	List<Order> queryLeftOrders(Order record);

	@Select("select count(*) from orders where animalId=#{animalId} and time=#{time} and role=0 and flag=0")
	int queryBuySize(@Param("animalId") Integer animalId, @Param("time") String time);

	@Select("select count(*) from orders where animalId=#{animalId} and time=#{time} and role=0 and state=2 and flag=0")
	int querySuccessBuySize(@Param("animalId") Integer animalId, @Param("time") String time);
	
	



}
