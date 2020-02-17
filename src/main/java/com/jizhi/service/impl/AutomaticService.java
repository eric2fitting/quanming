
package com.jizhi.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jizhi.dao.FeedDao;
import com.jizhi.dao.OrderTimeDao;
import com.jizhi.pojo.Animal;
import com.jizhi.pojo.Feed;
import com.jizhi.pojo.Match;
import com.jizhi.pojo.Order;
import com.jizhi.pojo.OrderTime;
import com.jizhi.pojo.Property;
import com.jizhi.pojo.User;
import com.jizhi.pojo.vo.ShowInfo;
import com.jizhi.service.AnimalService;
import com.jizhi.service.MatchService;
import com.jizhi.service.OrderService;
import com.jizhi.service.PropertyService;
import com.jizhi.service.UserSevice;

@Component
@Configuration
@EnableScheduling
public class AutomaticService {
	@Autowired
	private AnimalService animalService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private PropertyService propertyService;
	
	@Autowired
	private MatchService matchService;
	@Autowired
	private OrderTimeDao orderTimeDao;
	@Autowired
	private UserSevice userSevice;
	@Autowired
	private FeedDao feedDao;
	
	private static final Logger log = LoggerFactory.getLogger(AutomaticService.class);
	/**
	 * 自动匹配预约用户和资产拥有者
	 */
	@Scheduled(fixedRate=60*1000*3)
	public void match() {
		System.out.println("自动匹配开始执行");
		log.info("自动匹配开始执行");
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm");
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");	
		try {
			//现在的时间
			Date nowDate=simpleDateFormat1.parse(simpleDateFormat1.format(new Date()));
			//首页展示所有动物时间等信息
			List<ShowInfo> list = animalService.queryAnimalList();	
			//用于封装可以预约的动物及时间信息
			ArrayList<OrderTime> orderTimes = new ArrayList<OrderTime>();
			for(ShowInfo showInfo:list) {
				//一种动物对应的所有的预约领养时间段
				List<OrderTime> list2 = showInfo.getList();
				for(OrderTime orderTime:list2) {
					//开始预约的时间
					String startTime = orderTime.getStartTime();
					//结束预约的时间
					String endTime = orderTime.getEndTime();
					//当前时间在预约时间段之间
					if(simpleDateFormat1.parse(startTime).before(nowDate) && 
							simpleDateFormat1.parse(endTime).after(nowDate)) {
						orderTimes.add(orderTime);
						continue;//跳出本次循环
					}
				}
			}
			if(orderTimes.size()>0) {
				for(OrderTime orderTime:orderTimes) {
					Integer animalId = orderTime.getAnimalId();
					String startTime = orderTime.getStartTime();
					Animal animal = animalService.queryById(animalId);
					//根据动物id和开始时间去查找预约信息
					//根据动物id、预约日期、时间查询预约单
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("animalId", animalId);
					map.put("time", startTime);
					//今天的日期
					String nowDateString=simpleDateFormat2.format(new Date());
					map.put("date", nowDateString);
					//预约了该时间的所有预约信息
					List<Order> orders=this.orderService.queryAll(map);
					//根据动物id，当前时间查询可售的资产表
					Calendar instance = Calendar.getInstance();
					instance.add(Calendar.DATE, -animal.getCycle());
					String buyDate = simpleDateFormat2.format(instance.getTime());
					String buyTime=startTime;
					HashMap<String, Object> map1 = new HashMap<String, Object>();
					map1.put("animalId", animalId);
					map1.put("buyDate", buyDate);
					map1.put("buyTime", buyTime);
					Random random1=new Random();
					//当前时间该动物所有可以被卖的资产信息
					List<Property> properties=propertyService.queryCanSell(map1);
					//预约人数
					int orderSize = orders.size();
					//可售人数
					int sellSize = properties.size();
					//查询properties中有没有卖价要超过最大值的，取出全部匹配给管理员
					for(int a=0;a<sellSize;a++) {
						Property p0=properties.get(0);
						//价格超过了最大值
						if(judge(p0)) {
							//匹配给管理员并从集合中取出
							matchService.doMatch(newAdminOrder(p0.getAnimalId(), nowDateString, buyTime), p0);
							properties.remove(p0);
						}
					}
					//现在可售人数
					sellSize = properties.size();
					//当买家多余卖家
					if(orderSize>sellSize) {
						for(int i=0;i<sellSize;i++) {
							int number = random1.nextInt(orderSize);
							//随机取出买家
							Order order0=orders.get(number);
							Property property0=properties.get(0);
							//买家卖家不是同一个人
							if(order0.getUserId()!=property0.getUserId()) {
								matchService.doMatch(order0, property0);
								orderSize--;
								orders.remove(order0);
								properties.remove(property0);
							}else {
								//买家卖家是同一个人，让他匹配其他买家
								if(number==orderSize-1) {
									matchService.doMatch(orders.get(0), property0);
									orders.remove(orders.get(0));
									properties.remove(property0);
									orderSize--;
								}else {
									matchService.doMatch(orders.get(orderSize-1), property0);
									orders.remove(orders.get(orderSize-1));
									properties.remove(property0);
									orderSize--;
								}
							}
						}				
					}else if (sellSize==orderSize && sellSize!=0) {
						for(int i=0;i<sellSize;i++) {
							Order order0=orders.get(0);
							Property property0=properties.get(0);
							if(order0.getUserId()!=property0.getUserId()) {
								matchService.doMatch(order0, property0);
								orders.remove(order0);
								properties.remove(property0);
							}else {
								//如果当前买卖双方都只有一个人
								if(i==sellSize-1) {
									//将卖方匹配给管理员，买方不管
									//查询管理员账户
									List<User> users=userSevice.queryAdmin();
									int num=users.size();
									Random random = new Random();
									Order adminOrder = new Order();
									adminOrder.setAnimalId(animalId);
									//随机匹配一个管理员
									int nextInt = random.nextInt(num);
									adminOrder.setUserId(users.get(nextInt).getId());
									adminOrder.setRole(1);
									adminOrder.setDate(nowDateString);
									adminOrder.setTime(buyTime);
									adminOrder.setState(1);
									orderService.save(adminOrder);
									matchService.doMatch(adminOrder,property0);
								}else {
									//买卖双方还有多人，直接将下一个买家给卖家
									matchService.doMatch(orders.get(1), property0);
									orders.remove(orders.get(1));
									properties.remove(property0);
								}	
							}
						}	
					}
					else if(sellSize>orderSize){
						//卖家大于买家
						for(int i=0;i<orderSize;i++) {
							Order order0=orders.get(0);
							Property property0=properties.get(0);
							if(order0.getUserId()!=property0.getUserId()) {
								matchService.doMatch(order0,property0);
								orders.remove(order0);
								properties.remove(property0);
							}else {
								Property property1=properties.get(1);
								matchService.doMatch(order0,property1);
								orders.remove(order0);
								properties.remove(property1);
							}
						}
						// 剩余的全部匹配给管理员				
						//查询管理员账户
						List<User> users=userSevice.queryAdmin();
						int num=users.size();
						Random random = new Random();
						for(int i=0;i<properties.size();i++) {
							Order order = new Order();
							order.setAnimalId(animalId);
							//随机匹配一个管理员
							int nextInt = random.nextInt(num);
							order.setUserId(users.get(nextInt).getId());
							order.setRole(1);
							order.setDate(nowDateString);
							order.setTime(buyTime);
							order.setState(1);
							orderService.save(order);
							matchService.doMatch(order,properties.get(i));	
						}
					}
				}	
			}
			
		} catch (Exception e) {
			System.out.println("自动匹配出错");
			log.info("自动匹配出错");
		}
		System.out.println("自动匹配执行结束");
		log.info("自动匹配执行结束");
	}
	
	/**
	 * 根据最后时间进行自动确认
	 */
	@Scheduled(fixedRate=60*1000*3)
	public void confirm() {
		try {
			System.out.println("自动确认开始执行");
			log.info("自动确认开始执行");
			SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm");
			SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
			String nowTime = simpleDateFormat1.format(new Date());//当前时间
			String nowDate = simpleDateFormat2.format(new Date());//当前日期		
			
			//首页展示所有动物时间等信息
			List<ShowInfo> list = animalService.queryAnimalList();	
			//用于封装过了最后预约时间的信息
			ArrayList<OrderTime> orderTimes = new ArrayList<OrderTime>();
			for(ShowInfo showInfo:list) {
				//一种动物对应的所有的预约领养时间段
				List<OrderTime> list2 = showInfo.getList();
				for(OrderTime orderTime:list2) {
					//结束预约的时间
					String endTime = orderTime.getEndTime();
					//当前时间过了最后预约时间
					if(simpleDateFormat1.parse(endTime).before(simpleDateFormat1.parse(nowTime))) {
						orderTimes.add(orderTime);
					}
				}
			}
			for(OrderTime orderTime:orderTimes) {
				Integer animal_Id=orderTime.getAnimalId();
				//根据动物id,预约时间更改所有预约信息的状态未失败。管理员的除外
				Order order = new Order();
				order.setAnimalId(animal_Id);
				order.setDate(nowDate);
				order.setTime(orderTime.getStartTime());
				order.setRole(0);
				order.setState(0);
				//根据动物id,预约时间查看所有预约失败的order
				List<Order> failedOrders=orderService.queryFailedOrder(order);
				if(failedOrders.size()>0) {
					//退回预约的饲料
					Animal failedAnimal = animalService.queryById(animal_Id);
					BigDecimal max_b = new BigDecimal(failedAnimal.getMaxPrice());
					BigDecimal aa = new BigDecimal(3);
					BigDecimal bb = new BigDecimal(100);
					Double feed_Num=max_b.multiply(aa).divide(bb).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					for(Order failedOrder:failedOrders) {
						Feed feed = new Feed();
						feed.setDate(new Date());
						feed.setNum(feed_Num);
						feed.setType(6);
						feed.setUserId(failedOrder.getUserId());
						feedDao.insert(feed);
					}
					//将状态修改为失败
					orderService.updateToFail(order);
				}
			}
			//查询匹配表里所有双方都没有确认的消息
			List<Match> matches=matchService.queryAllByBuyerConfirm();
			if(matches.size()>0) {
				for(Match match:matches) {
					//查询该匹配表对应的预约最后时间是否过了，如果过了就冻结买家，并将卖家信息匹配给后台管理员。
					Integer propertyId = match.getPropertyId();
					Integer orderId = match.getOrderId();
					Order order = orderService.queryByOrderId(orderId);
					Property property = propertyService.queryById(propertyId);
					Integer animalId = property.getAnimalId();
					String startTime = property.getBuyTime();
					//根据动物id和买入时间计算该动物最后的交易时间
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("animalId",animalId);
					map.put("startTime",startTime);
					String endTime = orderTimeDao.queryLastTime(map);
					//当前时间过了最后的交易确认时间
					if(simpleDateFormat1.parse(nowTime).after(simpleDateFormat1.parse(endTime))) {
						//买卖双方都不是管理员
						if(order.getRole()==0 && property.getRole()==0) {
							//冻结买家
							map.put("userId", order.getUserId());
							map.put("isFrozen", 1);
							userSevice.updateIsFrozen(map);
							//删除原有的匹配信息
							matchService.deleteById(match.getId());
								
							ArrayList<Date> startTimest=new ArrayList<Date>();//存放所有开始时间
							//更改卖家的买入时间到下一轮匹配
							String buyTime=property.getBuyTime();
							Date buyTime1=simpleDateFormat1.parse(buyTime);
							List<OrderTime> times=orderTimeDao.queryByAnimalId(animalId);
							for(OrderTime orderTime:times) {
								startTimest.add(simpleDateFormat1.parse(orderTime.getStartTime()));
							}
							Collections.sort(startTimest);
							int index = startTimest.indexOf(buyTime1);
							if(index==startTimest.size()-1) {//买入时间是今天的最后时间
								//买入时间是今天的最后时间，将该动物的购买时间换成第二天的第一个时间段
								String changtime=simpleDateFormat1.format(startTimest.get(0));
								HashMap<String,Object> map1=new HashMap<String,Object>();
								map1.put("id", property.getId());
								map1.put("buyTime", changtime);
								//计算买入时间的第二天是多久
								String oldDate=property.getBuyDate();
								Calendar c1=Calendar.getInstance();
								c1.setTime(simpleDateFormat2.parse(oldDate));
								c1.add(Calendar.DAY_OF_YEAR, 1);
								Date changed = c1.getTime();
								String newDate = simpleDateFormat2.format(changed);
								map1.put("buyDate",newDate);
								propertyService.updateBuyDateTime(map1);
							}else {
								//将买入时间换成下一个时间段
								Date changDate=startTimest.get(index+1);
								String changTime = simpleDateFormat1.format(changDate);
								HashMap<String,Object> map1=new HashMap<String,Object>();
								map1.put("id", property.getId());
								map1.put("buyTime", changTime);
								map1.put("buyDate",property.getBuyDate());
								propertyService.updateBuyDateTime(map1);
							}
						}	
					}
				}
			}
			//查询匹配表里卖家没有确认的消息
			List<Match> matches1=matchService.queryAllBySellerConfirm();
			if(matches1.size()>0) {
				for(Match match:matches1) {
					//判断当前时间是否过了交易确认的最后时间
					Integer propertyId = match.getPropertyId();
					Property property = propertyService.queryById(propertyId);
					Integer animalId = property.getAnimalId();
					String startTime = property.getBuyTime();
					Order order01=orderService.queryByOrderId(match.getOrderId());
					//根据动物id和买入时间计算该动物最后的交易时间
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("animalId",animalId);
					map.put("startTime",startTime);
					String endTime = orderTimeDao.queryLastTime(map);
					if(simpleDateFormat1.parse(nowTime).after(simpleDateFormat1.parse(endTime))) {
						//买卖双方都不是管理员
						if(order01.getRole()==0 && property.getRole()==0) {
							propertyService.doSellDirectly(match.getId());
						}
						
					}
				}
			}
			
			System.out.println("自动确认执行结束");
			log.info("自动确认执行结束");
		}
		 catch (Exception e) {
			System.out.println("自动确认出错");
			log.info("自动确认出错");
		}
	}
	
	/**
	 * 定时每天凌晨1点删除所有预约记录
	 */
	@Scheduled(cron="0 0 1 * * ?")
	public void delete() {
		orderService.deleteAll();//软删除
	}
	
	
	
	public  Order newAdminOrder(Integer animalId,String nowDateString,String buyTime) {
		//查询管理员账户
		List<User> users=userSevice.queryAdmin();
		int num=users.size();
		Random random = new Random();
		Order adminOrder = new Order();
		adminOrder.setAnimalId(animalId);
		//随机匹配一个管理员
		int nextInt = random.nextInt(num);
		adminOrder.setUserId(users.get(nextInt).getId());
		adminOrder.setRole(1);
		adminOrder.setDate(nowDateString);
		adminOrder.setTime(buyTime);
		adminOrder.setState(1);
		orderService.save(adminOrder);
		return adminOrder;
	}
	
	/**
	 * 判断卖价是否超过最高价，超过了匹配给管理员
	 * @param property
	 * @return
	 */
	public boolean judge(Property property) {
		Integer sellAnimalId=property.getAnimalId();
		Animal sellAnimal = animalService.queryById(sellAnimalId);
		//计算动物的出栏价格
		BigDecimal bb1=new BigDecimal(property.getPrice());
		BigDecimal bb2=new BigDecimal(sellAnimal.getProfit());
		BigDecimal bb3=new BigDecimal(100D);
		Double sellMoney=bb1.multiply(bb2.add(bb3)).divide(bb3).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		//价格超过最高值的大型动物
		return sellMoney>=sellAnimal.getMaxPrice();
	}
}

