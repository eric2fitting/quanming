	package com.jizhi.service.impl;

    import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jizhi.dao.FeedDao;
import com.jizhi.dao.OrderDao;
import com.jizhi.dao.OrderTimeDao;
import com.jizhi.pojo.Animal;
import com.jizhi.pojo.Feed;
import com.jizhi.pojo.Order;
import com.jizhi.pojo.OrderTime;
import com.jizhi.pojo.vo.AnimaInfo;
import com.jizhi.pojo.vo.IsOrderOrOverTime;
import com.jizhi.pojo.vo.OrderDetail;
import com.jizhi.service.AnimalService;
import com.jizhi.service.OrderService;
import com.jizhi.util.RedisService;
@Transactional(rollbackFor = Exception.class)
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
	
	@Autowired
	private FeedDao feedDao;
	
	/**
	 * 添加预约
	 */
	@Override
	public int addOrder(Integer id,String token) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String string = redisService.get(token);
		Integer userId=Integer.parseInt(string);
		Order order = new Order();
		order.setUserId(userId);//设置预约订单的预约人id
		//根据orderTimeId查询时间动物id等
		OrderTime orderTime=orderTimeDao.queryById(id);
		Integer animalId = orderTime.getAnimalId();
		Animal animal = animalService.queryById(animalId);
		order.setTime(orderTime.getStartTime());//设置预约订单的开始时间
		order.setDate(simpleDateFormat.format(new Date()));//设置订单的日期
		order.setAnimalId(animalId);//设置订单的animalId
		order.setState(1);//设置订单状态为正在预约
		order.setRole(0);
		AnimaInfo info = toOrder(animalId, token);
		if(info.getFeedOwns()<info.getFeedNeed()) {
			return 2;
		}
		orderDao.save(order);
		BigDecimal b1 = new BigDecimal(animal.getMaxPrice());
		BigDecimal b2 = new BigDecimal(3);
		BigDecimal b3 = new BigDecimal(100);
		//预约所需要的饲料
		double feedNeed = b1.multiply(b2).divide(b3).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		//扣除相应饲料
		Feed feed = new Feed();
		feed.setDate(new Date());
		feed.setNum(-feedNeed);
		feed.setType(1);
		feed.setUserId(userId);
		feedDao.insert(feed);
		return 1;
	}
	
	/**
	 * 点击首页预约领养后，显示该动物的预约详情
	 */
	@Override
	public AnimaInfo toOrder(Integer animalId, String token) {
		//得到该动物所有的开始预约时间段
		List<OrderTime> orderTimes = orderTimeDao.queryByAnimalId(animalId);
		//得到该动物的信息
		Animal animal = animalService.queryById(animalId);
		//得到用户
		String user_id = this.redisService.get(token);
		Integer userId=Integer.parseInt(user_id);
		BigDecimal b1 = new BigDecimal(animal.getMaxPrice());
		BigDecimal b2 = new BigDecimal(3);
		BigDecimal b3 = new BigDecimal(100);
		//预约所需要的饲料
		double feedNeed = b1.multiply(b2).divide(b3).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		//用户拥有的饲料
		Double feedOwns = feedDao.queryTotalFeedByUserId(userId);
		//用AnimaInfo封装需要返回的信息
		AnimaInfo animaInfo = new AnimaInfo();
		animaInfo.setFeedNeed(feedNeed);
		if(feedOwns==null) {
			animaInfo.setFeedOwns(0D);
		}else {
			animaInfo.setFeedOwns(feedOwns);
		}
		animaInfo.setAnimalSize(animal.getSize());
		animaInfo.setAnimalType(animal.getAnimalType());
		ArrayList<IsOrderOrOverTime> list = new ArrayList<IsOrderOrOverTime>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
		for(OrderTime orderTime:orderTimes) {
			String startTime = orderTime.getStartTime();
			String endTime=orderTime.getEndTime();
			IsOrderOrOverTime isOrderOrOverTime = new IsOrderOrOverTime();
			isOrderOrOverTime.setOrderTimeId(orderTime.getId());
			isOrderOrOverTime.setStartTime(startTime);
			isOrderOrOverTime.setEndTime(endTime);
			try {
				//当前时间
				Date date = new Date();
				String nowDate=simpleDateFormat.format(date);
				//判断现在的时间在预约时间之前还是之后
				
				if(simpleDateFormat.parse(startTime).before(simpleDateFormat.parse(nowDate))) {
					isOrderOrOverTime.setState("0");//0过期
				}else {
					//判断是否已经预约过
					Order record = new Order();
					record.setAnimalId(animalId);
					record.setTime(startTime);
					record.setUserId(userId);
					Order order=orderDao.queryByUserIdAndTime(record);
					if(order==null) {
						isOrderOrOverTime.setState("2");;//表示可以预约
					}else {
						isOrderOrOverTime.setState("1");//2表示已经预约
					}
				}
				list.add(isOrderOrOverTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		animaInfo.setList(list);
		return animaInfo;

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
		if(orders.size()>0) {
			for(Order order:orders) {
				//根据动物id查询动物种类大小
				Animal animal = animalService.queryById(order.getAnimalId());
				//将预约详情封装到OrderDetail中
				OrderDetail orderDetail = new OrderDetail();
				orderDetail.setAnimalType(animal.getAnimalType());
				//预约时间：日期加时间段
				String startTime = order.getTime();
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("startTime", startTime);
				map.put("animalId", order.getAnimalId());
				String endTime = orderTimeDao.queryLastTime(map);
				orderDetail.setDate(order.getDate()+"  "+startTime+"-"+endTime);
				orderDetail.setSize(animal.getSize());
				orderDetail.setState(order.getState());
				list.add(orderDetail);
			}
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
	
	/**
	 * 根据预约id查询order信息
	 */
	@Override
	public Order queryByOrderId(Integer id) {
		return orderDao.queryById(id);
	}
	
	/**
	 * 添加order后返回主键
	 * @param order
	 * @return
	 */
	public Integer save(Order order) {
		return orderDao.insert(order);
	}

	@Override
	public void updateToFail(Order order) {
		orderDao.updateToFail(order);
		
	}

	@Override
	public void deleteAll() {
		orderDao.deleteAll();
		
	}
	
	/**
	 * 查询预约失败的order
	 */
	@Override
	public List<Order> queryFailedOrder(Order order) {
		return orderDao.queryFailedOrder(order);
	}

	
	

}
