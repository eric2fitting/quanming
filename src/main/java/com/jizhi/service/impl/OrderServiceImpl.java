	package com.jizhi.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jizhi.dao.OrderDao;
import com.jizhi.dao.OrderTimeDao;
import com.jizhi.pojo.Animal;
import com.jizhi.pojo.Order;
import com.jizhi.pojo.vo.OrderDetail;
import com.jizhi.service.AnimalService;
import com.jizhi.service.OrderService;
import com.jizhi.util.RedisService;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private OrderTimeDao orderTimeDao;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private AnimalService animalService;
	
	/**
	 * 添加预约
	 */
	@Override
	public int addOrder(Order order) {
		int i=this.orderDao.save(order);
		return i;
	}
	
	/**
	 * 点击首页预约领养后，显示该动物的预约详情
	 */
	@Override
	public List<Integer> toOrder(Integer animalId, String token) {
		//TODO 得到该动物所有的开始预约时间，去除的时间是按早到晚的顺序
		List<String> startTimes = this.orderTimeDao.queryStartTimeByAnimalId(animalId);
		//得到用户
		String user_id = this.redisService.get(token);
		Integer userId=Integer.parseInt(user_id);
		ArrayList<Integer> codes = new ArrayList<Integer>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
		for(String startTime:startTimes) {
			//判断今天该时段是否已经预约
			Order order=this.orderDao.queryByUserIdAndTime(userId,startTime);
			if(order!=null) {
				codes.add(2);//说明已经预约过
			}else {
				//开始时间在当前时间之后
				try {
					if(simpleDateFormat.parse(startTime).after(new Date())) {
						codes.add(1);
					}else {
						//开始时间在当前时间之前
						codes.add(0);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
		}
		return codes;
	}


	
	
	/**
	 * 查询所有预约信息
	 */
	
	@Override
	public List<Order> queryAll(HashMap<String, Object> map) {
		return this.orderDao.queryAll(map);
	}
	
	
	/**
	 * 匹配成功，预约状态更改为预约成功
	 */
	@Override
	public void updateState(Integer id) {
		orderDao.updateState(id);
		
	}
	
	
	/**
	 * 查询该用户的所有预约信息
	 */
	@Override
	public List<OrderDetail> queryAllByUserId(String token) {
		String string = redisService.get(token);
		Integer userId=Integer.parseInt(string);
		List<Order> orders=orderDao.queryAllByUserId(userId);
		List<OrderDetail> list=new ArrayList<OrderDetail>();
		for(Order order:orders) {
			//根据动物id查询动物种类大小
			Animal animal = animalService.queryById(order.getAnimalId());
			//将预约详情封装到OrderDetail中
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setAnimalType(animal.getAnimalType());
			orderDetail.setDate(order.getDate());
			orderDetail.setSize(animal.getSize());
			orderDetail.setState(order.getState());
			list.add(orderDetail);
		}
		return list;
	}
	
	
	/**
	 * 根据用户id查询他所有的成功预约信息
	 */
	@Override
	public List<Order> querySuccessOrder(Integer userId) {
		return orderDao.querySuccessOrder(userId);
	}
	
	

}
