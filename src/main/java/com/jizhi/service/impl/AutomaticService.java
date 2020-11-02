
package com.jizhi.service.impl;
/**
 * 定时任务时间：8点05份、10点05份、10点10份、11点10份、11点30、11点40
 */

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jizhi.dao.AnimalDao;
import com.jizhi.dao.FeedDao;
import com.jizhi.dao.MatchDao;
import com.jizhi.dao.MatchDetailDao;
import com.jizhi.dao.OrderDao;
import com.jizhi.dao.OrderTimeDao;
import com.jizhi.dao.ProfitsDao;
import com.jizhi.dao.PropertyDao;
import com.jizhi.dao.UserDao;
import com.jizhi.pojo.Animal;
import com.jizhi.pojo.Feed;
import com.jizhi.pojo.Match;
import com.jizhi.pojo.MatchDetail;
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
	@Autowired
	private MatchDao matchDao;
	@Autowired
	private AnimalDao animalDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private MatchDetailDao matchDetailDao;
	@Autowired
	private PropertyDao propertyDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ProfitsDao profitsDao;
	
	private static final Logger log = LoggerFactory.getLogger(AutomaticService.class);
	/**
	 * 自动匹配预约用户和资产拥有者
	 */
	@Scheduled(cron="0 01 10 * * ?")
	@Scheduled(cron="0 01 13 * * ?")
	@Scheduled(cron="0 01 16 * * ?")
	public void match() {
		System.out.println("自动匹配开始执行");
		long startWorkTime=System.currentTimeMillis();
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
					//查询properties中有没有卖价要超过最大值的，取出全部匹配给管理员
					for(int a=0;a<properties.size();a++) {
						Property p0=properties.get(a);
						//价格超过了最大值
						if(judge(p0)) {
							//匹配给管理员并从集合中取出
							matchService.doMatch(newAdminOrder(p0.getAnimalId(), nowDateString, buyTime), p0);
							properties.remove(p0);
							a--;
						}
					}
					//现在可售人数
					int sellSize = properties.size();
					//当买家多余卖家
					if(orderSize>sellSize) {
						for(int i=0;i<sellSize;i++) {
							int number = random1.nextInt(orderSize);
							//随机取出买家
							Order order0=orders.get(number);
							Property property0=properties.get(0);
							//买家卖家不是同一个人
							if(!order0.getUserId().equals(property0.getUserId())) {
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
							if(!order0.getUserId().equals(property0.getUserId())) {
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
							if(!order0.getUserId().equals(property0.getUserId())) {
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
			log.info(e.getMessage());
			log.info("自动匹配出错");
		}
		System.out.println("自动匹配执行结束");
		log.info("自动匹配执行结束,用时："+(System.currentTimeMillis()-startWorkTime)/1000.0);
	}
	
	/**
	 * 根据最后时间进行自动确认
	 */
	@Scheduled(cron="0 01 12 * * ?")
	@Scheduled(cron="0 01 15 * * ?")
	@Scheduled(cron="0 01 18 * * ?")
	public void confirm() {
		try {
			System.out.println("自动确认开始执行");
			long startWorkTime=System.currentTimeMillis();
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
							//将买家所有资产变为不可售
							propertyDao.updateCansellByUserId(order.getUserId());
							//删除原有的匹配信息
							matchService.deleteById(match.getId());
							
							//更改property状态为0
							property.setIsSold(0);
							propertyDao.updateState(property);
							//将用户该时段的改类型的商铺全部延后一天
							List<Property> updateList = propertyDao.queryByUserIdAndBuyTime(property.getAnimalId(),property.getUserId(),property.getBuyTime());
							int sum=updatePropertiesBuyDate(updateList);
							log.info(String.format("用户：%s商铺id:%s，时间：%s，买入时间修改了%s条", property.getUserId(),property.getAnimalId(),property.getBuyTime(),sum));
//							ArrayList<Date> startTimest=new ArrayList<Date>();//存放所有开始时间
//							//更改卖家的买入时间到下一轮匹配
//							String buyTime=property.getBuyTime();
//							Date buyTime1=simpleDateFormat1.parse(buyTime);
//							List<OrderTime> times=orderTimeDao.queryByAnimalId(animalId);
//							for(OrderTime orderTime:times) {
//								startTimest.add(simpleDateFormat1.parse(orderTime.getStartTime()));
//							}
//							Collections.sort(startTimest);
//							int index = startTimest.indexOf(buyTime1);
//							if(index==startTimest.size()-1) {//买入时间是今天的最后时间
//								//买入时间是今天的最后时间，将该动物的购买时间换成第二天的第一个时间段
//								String changtime=simpleDateFormat1.format(startTimest.get(0));
//								HashMap<String,Object> map1=new HashMap<String,Object>();
//								map1.put("id", property.getId());
//								map1.put("buyTime", changtime);
//								//计算买入时间的第二天是多久
//								String oldDate=property.getBuyDate();
//								Calendar c1=Calendar.getInstance();
//								c1.setTime(simpleDateFormat2.parse(oldDate));
//								c1.add(Calendar.DAY_OF_YEAR, 1);
//								Date changed = c1.getTime();
//								String newDate = simpleDateFormat2.format(changed);
//								map1.put("buyDate",newDate);
//								propertyService.updateBuyDateTime(map1);
//							}else {
//								//将买入时间换成下一个时间段
//								Date changDate=startTimest.get(index+1);
//								String changTime = simpleDateFormat1.format(changDate);
//								HashMap<String,Object> map1=new HashMap<String,Object>();
//								map1.put("id", property.getId());
//								map1.put("buyTime", changTime);
//								map1.put("buyDate",property.getBuyDate());
//								propertyService.updateBuyDateTime(map1);
//							}
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
			log.info("自动确认执行结束,用时："+(System.currentTimeMillis()-startWorkTime)/1000.0);
		}
		 catch (Exception e) {
			System.out.println("自动确认出错");
			log.info(e.getMessage());
			log.info("自动确认出错");
		}
	}
	
	/**
	 * 定时每天晚上11点30删除所有预约记录
	 */
	@Scheduled(cron="0 30 23 * * ?")
	public void delete() {
		log.info("软删除预约记录");
		orderService.deleteAll();//软删除
	}
	
	/**
	 * 定时每天晚上11点30删除所有买家未付款的订单
	 */
	@Scheduled(cron="0 40 23 * * ?")
	public void deleteNotPay() {
		log.info("删除所有未付款的匹配记录");
		matchDao.deleteNotPay();
	}
	
	/**
	 * 10点12自动将多余玩家按比例匹配给管理员
	 */
	@Scheduled(cron="0 06 10 * * ?")
	public void leftMatch1() {
		autoMatchLeft("10:00");
	}
	
	/**
	 * 13点12自动将多余玩家按比例匹配给管理员
	 */
	@Scheduled(cron="0 06 13 * * ?")
	public void leftMatch2() {
		autoMatchLeft("13:00");
	}
	
	/**
	 * 16点12自动将多余玩家按比例匹配给管理员
	 */
	@Scheduled(cron="0 06 16 * * ?")
	public void leftMatch3() {
		autoMatchLeft("16:00");
	}
	
	public void autoMatchLeft(String buyTime) {
		try {
			log.info("多余订单开始匹配"+buyTime+"---------");
			List<Animal> animals = animalDao.queryAll();
			List<User> users=userSevice.queryAdmin();
			SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
			int j=0;
			for(Animal animal :animals) {
				Order record=new Order();
				record.setAnimalId(animal.getId());
				record.setTime(buyTime);
				List<Order> leftOrders=orderDao.queryLeftOrders(record);
				MatchDetail md=matchDetailDao.queryByAnimalId(animal.getId());
				Calendar instance = Calendar.getInstance();
				instance.add(Calendar.DATE, -animal.getCycle());
				String buyDate = simpleDateFormat2.format(instance.getTime());
				//预约成功的普通玩家人数
				int successBuySize=orderDao.querySuccessBuySize(animal.getId(),buyTime);
				log.info("成功预约的人数"+successBuySize+"---id"+animal.getId());
				//普通买家人数
				int buySize=orderDao.queryBuySize(animal.getId(),buyTime);
				log.info("买家人数"+buySize+"---id"+animal.getId());
				Integer rate=md.getRate();
				Double minPrice=md.getMinPrice();
				Double maxPrice=md.getMaxPrice();
				BigDecimal b1=new BigDecimal(rate);
				BigDecimal b2=new BigDecimal(100);
				int orderSize=leftOrders.size();
				BigDecimal b3=new BigDecimal(buySize);
				//允许的最大买家数量
				int canBuyNum = b3.multiply(b1).divide(b2).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
				log.info("允许最大买家数量"+canBuyNum+"---id"+animal.getId());
				if(canBuyNum>successBuySize) {
					//还应匹配的玩家人数
					int buyNum=canBuyNum-successBuySize;
					log.info("管理员应再放数量+"+buyNum+"---id"+animal.getId());
					for(int i=0;i<buyNum;i++) {
						Random random = new Random();
						int nextInt = random.nextInt(orderSize);
						Property property = new Property();
						property.setAnimalId(animal.getId());
						property.setBuyTime(buyTime);
						property.setCode(animalService.queryNumber());
						property.setIsSold(0);
						property.setRole(1);
						if(j==users.size()) {
							j=0;
						}
						property.setUserId(users.get(j).getId());
						Double price=new BigDecimal(Math.random()*(maxPrice-minPrice)+minPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						property.setPrice(price);
						
						property.setBuyDate(buyDate);
						propertyDao.add(property);
						Order o1=leftOrders.get(nextInt);
						matchService.doMatch(o1, property);
						leftOrders.remove(o1);
						j++;
						orderSize--;
					}
				}
			}
			log.info("多余订单匹配成功---"+buyTime);
		} catch (Exception e) {
			log.info(e.getMessage());
			log.info("多余订单匹配失败---"+buyTime);
		}
		
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
	
	/**
	 * 每晚8：05更新所有普通用户的总资产
	 */
	@Scheduled(cron="0 05 20 * * ?")
	public void updateTotalMoney() {
		log.info("开始更新个人总资产");
		Long l1=System.currentTimeMillis();
		try {
			List<User> users=userDao.queryAll();
			for(User user:users) {
				Integer userId=user.getId();
				//查找所有资产
				Double totalProperty = propertyDao.queryTotalMonet(userId);
				if(totalProperty==null) {
					totalProperty=0D;
				}
				//总nfc
				Double totalNFC = profitsDao.queryAllNFC(userId);
				if(totalNFC==null) {
					totalNFC=0D;
				}
				//总分享收益
				Double totalShare = profitsDao.queryShareProfit(userId);
				if(totalShare==null) {
					totalShare=0D;
				}
				Double total=totalProperty+totalNFC+totalShare;
				user.setTotalMoney(total);
				//更新总资产
				userDao.updateTotalMoney(user);
			}
		} catch (Exception e) {
			log.info("更新个人总资产出错");
		}
		log.info("更新个人总资产完成,用时："+(System.currentTimeMillis()-l1));
	}
	
	/**
	 * 每晚22：05更新用户的等级，检查今天是否有预约。
	 */
	@Scheduled(cron="0 05 22 * * ?")
	//@Scheduled(fixedRate=60*1000*5)
	public void checkUnActiveUser() {
		log.info("开始检查用户及下级的活跃状态");
		long l1 = System.currentTimeMillis();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String today = simpleDateFormat.format(date);
		List<User> users=userDao.queryAll();
		//按等级的升序排列，o1
		Collections.sort(users,new Comparator<User>() {
			@Override
			public int compare(User o1, User o2) {
				return o1.getLevel()-o2.getLevel();
			}
		});
		log.info("最低等级----"+users.get(0).getLevel());
		log.info("最高等级----"+users.get(users.size()-1).getLevel());
		for(User user:users) {
			//只需判断活跃用户
			if(StringUtils.equals("活跃", user.getState())) {
				List<Order> orders = orderDao.queryByUserIdAndDate(user.getId(),today);
				if(orders.size()==0) {
					//说明该用户今日没预约，改为已激活
					user.setState("已激活");
					userDao.updateStateToUnActive(user);
					//上级用户等级是否受影响
					propertyService.updateOlderUserLevel(user);
				}
			}
		}
		log.info("用户及下级的活跃状态检查完毕,用时："+(System.currentTimeMillis()-l1)+"毫秒");
	}
	
	/**
	 * 每晚23点10分判断是否将资产出售的条件变为1
	 */
	@Scheduled(cron="0 10 23 * * ?")
	public void forbidProperty() {
		log.info("开始跟新资产可售状态");
		User superAdmin = userDao.queryById(501);
		int a=0;
		if(superAdmin!=null) {
			//超级管理员的isConfirmed为1时表示要去用户必须预约了才能出售，所以每个资产变为不可出售
			if(superAdmin.getIsConfirmed().equals(1)) {
				a=propertyDao.updateCanSell();
			}
		}
		log.info("跟新资产可售状态结束,更新条数："+a);
	}
	
	/**
	 * 延后商铺的买入时间1天
	 * @throws Exception
	 */
	@Scheduled(cron="0 10 22 * * ?")
	public void updateBuyDate() throws Exception {
		log.info("开始跟新资产买入时间");
		long l1=System.currentTimeMillis();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String today = simpleDateFormat.format(new Date());
		List<Animal> allAnimal = animalDao.queryAll();
		Set<Property> updateSet = new HashSet<Property>();
		int count=0;
		for(Animal animal:allAnimal) {
			Integer waitDays = animal.getCycle();
			Integer animalId = animal.getId();
			Calendar c1=Calendar.getInstance();
			c1.setTime(simpleDateFormat.parse(today));
			c1.add(Calendar.DAY_OF_YEAR, -waitDays);
			Date changed = c1.getTime();
			String buyDay = simpleDateFormat.format(changed);
			List<Property> queryNotOrderAndSellList = propertyDao.queryNotOrderAndSellList(animalId,buyDay);
			for(Property property:queryNotOrderAndSellList) {
				List<Property> properties=propertyDao.queryByUserIdAndBuyTime(animalId,property.getUserId(),property.getBuyTime());
				updateSet.addAll(properties);
			}
		}
		count=updatePropertiesBuyDate(updateSet);
		log.info("跟新资产买入时间结束,更新条数："+count+"条,用时："+(System.currentTimeMillis()-l1)/1000.0);
	}
	
	/**
	 * 把所有的动物买入时间延后一天
	 * @param collection
	 * @return
	 * @throws Exception
	 */
	private int updatePropertiesBuyDate(Collection<Property> collection) throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		int sum=0;
		for(Property property:collection) {
			String oldDate=property.getBuyDate();
			Calendar c1=Calendar.getInstance();
			c1.setTime(simpleDateFormat.parse(oldDate));
			c1.add(Calendar.DAY_OF_YEAR, 1);
			Date changed = c1.getTime();
			String newDate = simpleDateFormat.format(changed);
			int a=propertyDao.updateBuyDateById(newDate,property.getId());
			sum+=a;
		}
		return sum;
	}
	
//	@Scheduled(fixedRate=60*10000000*60)
//	public void updateBuyTime() throws Exception {
//		log.info("开始改变买入时间");
//		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
//		List<Property> list = propertyDao.queryAllNotSell();
//		int i=0;
//		for(Property property:list) {
//			String oldDate=property.getBuyDate();
//			Calendar c1=Calendar.getInstance();
//			c1.setTime(simpleDateFormat2.parse(oldDate));
//			c1.add(Calendar.DAY_OF_YEAR, 3);
//			Date changed = c1.getTime();
//			String newDate = simpleDateFormat2.format(changed);
//			int a=propertyDao.updateBuyDateById(newDate,property.getId());
//			i+=a;
//		}
//		
//		log.info("全部修改完成："+i);
//	}
	
}

