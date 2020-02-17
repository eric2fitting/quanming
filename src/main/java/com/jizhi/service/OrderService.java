package com.jizhi.service;

import com.jizhi.pojo.Order;
import com.jizhi.pojo.vo.AnimaInfo;
import com.jizhi.pojo.vo.OrderDetail;

import java.util.HashMap;
import java.util.List;

public interface OrderService{

	int addOrder(Integer id,String token);

	AnimaInfo toOrder(Integer animalId, String token);

	List<Order> queryAll(HashMap<String, Object> map);

	void updateState(Integer orderId);

	List<OrderDetail> queryAllByUserId(String token);
	
	List<Order> querySuccessOrder(Integer buyerId);

	Order queryByOrderId(Integer orderId);
	
	/**
	 * 添加order后返回主键
	 * @param order
	 * @return
	 */
	Integer save(Order order);
	//预约状态更改未失败
	void updateToFail(Order order);
	//删除所有定时任务
	void deleteAll();

	List<Order> queryFailedOrder(Order order);

}
