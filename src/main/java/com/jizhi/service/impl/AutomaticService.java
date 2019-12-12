
package com.jizhi.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jizhi.dao.OrderTimeDao;
import com.jizhi.dao.UserDao;
import com.jizhi.pojo.Animal;
import com.jizhi.pojo.Match;
import com.jizhi.pojo.Order;
import com.jizhi.pojo.OrderTime;
import com.jizhi.pojo.Profits;
import com.jizhi.pojo.Property;
import com.jizhi.pojo.User;
import com.jizhi.pojo.vo.ShowInfo;
import com.jizhi.service.AnimalService;
import com.jizhi.service.MatchService;
import com.jizhi.service.OrderService;
import com.jizhi.service.ProfitsService;
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
	private UserDao userDao;
	@Autowired
	private ProfitsService profitsService;
	
	/**
	 * 自动匹配预约用户和资产拥有者
	 */
	@Scheduled(fixedRate=60*1000*10)
	public void match() {
		System.out.println("自动匹配开始执行");
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
					String endTime = orderTime.getStartTime();
					//当前时间在预约时间段之间
					if(simpleDateFormat1.parse(startTime).before(nowDate) && 
							simpleDateFormat1.parse(endTime).after(nowDate)) {
						orderTimes.add(orderTime);
						continue;//跳出本次循环
					}
				}
			}
			if(orderTimes!=null) {
				for(OrderTime orderTime:orderTimes) {
					Integer animalId = orderTime.getAnimalId();
					String startTime = orderTime.getStartTime();
					Animal animal = animalService.queryById(animalId);
					//根据动物id和开始时间去查找预约信息
					//根据动物id、预约日期、时间查询预约单
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("animalId", animalId);
					map.put("time", startTime);
					map.put("date", simpleDateFormat2.format(new Date()));
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
					//当前时间该动物所有可以被卖的资产信息
					List<Property> properties=propertyService.queryCanSell(map1);
					//预约人数
					int orderSize = orders.size();
					//可售人数
					int sellSize = properties.size();
					//当预约人数多余可售人数时
					if(orderSize>sellSize) {
						for(int i=0;i<sellSize;i++) {
							matchService.doMatch(orders.get(i),properties.get(i));
						}
						//TODO 剩余的管理员手动操作
						
					}else {
						//可售人数大于或等于预约人数
						for(int i=0;i<orderSize;i++) {
							matchService.doMatch(orders.get(i),properties.get(i));	
						}
						// 剩余的全部匹配给管理员				
						if((sellSize-orderSize)>0) {
							//查询管理员账户
							List<User> users=userSevice.queryAdmin();
							int num=users.size();
							Random random = new Random();
							for(int i=orderSize;i<sellSize;i++) {
								Order order = new Order();
								order.setAnimalId(animalId);
								//随机匹配一个管理员
								int nextInt = random.nextInt(num);
								order.setUserId(users.get(nextInt).getId());
								matchService.doMatch(order,properties.get(i));	
							}
						}
					}
				}	
			}
			
		} catch (Exception e) {
			System.out.println("自动匹配出错");
		}
		System.out.println("自动匹配执行结束");
	}
	
	/**
	 * 根据最后时间进行自动确认
	 */
	@Scheduled(fixedRate=60*1000*30)
	public void confirm() {
		try {
			System.out.println("自动确认开始执行");
			SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm");
			SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
			String nowTime = simpleDateFormat1.format(new Date());
			//查询匹配表里所有双方都没有确认的消息
			List<Match> matches=matchService.queryAllByBuyerConfirm();
			if(matches!=null) {
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
						//冻结买家
						map.put("userId", order.getUserId());
						map.put("isFrozen", 1);
						userSevice.updateIsFrozen(map);
						//TODO 并将卖家匹配给管理员
						
					}
				}
			}
			//查询匹配表里卖家没有确认的消息
			List<Match> matches1=matchService.queryAllBySellerConfirm();
			if(matches1!=null) {
				for(Match match:matches1) {
					//判断当前时间是否过了交易确认的最后时间
					Integer propertyId = match.getPropertyId();
					Integer orderId = match.getOrderId();
					Order order = orderService.queryByOrderId(orderId);
					Integer buyerId = order.getUserId();//买家id
					Property property = propertyService.queryById(propertyId);
					Integer sellerId = property.getUserId();//卖家id
					Integer animalId = property.getAnimalId();
					String startTime = property.getBuyTime();
					//根据动物id和买入时间计算该动物最后的交易时间
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("animalId",animalId);
					map.put("startTime",startTime);
					String endTime = orderTimeDao.queryLastTime(map);
					if(simpleDateFormat1.parse(nowTime).after(simpleDateFormat1.parse(endTime))) {
						//系统自动确认匹配状态
						matchService.updateSellerConfirm(match.getId());
						//更改资产表里的状态-变为已卖
						propertyService.updateToSold(propertyId);
						//还需要往资产表里添加新的数据
						Animal animal = animalService.queryById(animalId);
						//动物的下次出售的价格超过了该种类的最大值
						Double buyPrice = match.getPrice();
						Double maxPrice = animal.getMaxPrice();
						Integer profit = animal.getProfit();
						BigDecimal b1 = new BigDecimal(buyPrice);
						BigDecimal b2 = new BigDecimal(profit);
						BigDecimal b3 = new BigDecimal(5D);
						BigDecimal b4 = new BigDecimal(100D);
						Integer newAnimalId=animalId;
						Double sellPrice=b1.multiply(b2.subtract(b3)).divide(b4, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
						if(sellPrice>maxPrice || sellPrice==maxPrice) {
							HashMap<String,Object> map1=new HashMap<String,Object>();
							//改变尺寸
							if(animal.getSize().equals("小型")) {
								map1.put("size", "中型");
								map1.put("animalType", animal.getAnimalType());
								newAnimalId=animalService.queryAnimalId(map1);
							}else if(animal.getSize().equals("中型")) {
								map1.put("size", "大型");
								map1.put("animalType", animal.getAnimalType());
								newAnimalId=animalService.queryAnimalId(map1);
							}
						}
						Property property3 = new Property();
						property3.setAnimalId(newAnimalId);
						property3.setBuyDate(simpleDateFormat2.format(new Date()));
						property3.setBuyTime(property.getBuyTime());
						property3.setIsSold(0);
						property3.setUserId(buyerId);
						property3.setPrice(buyPrice);
						propertyService.add(property3);
						//往利润表里添加信息
						Profits profits = new Profits();
						profits.setUserId(sellerId);
						profits.setNFC(animal.getNfc());
						//计算卖家卖了的收益
						Double p1=property.getPrice();//卖家的买价
						Double p2=match.getPrice();	//卖家的卖价
						BigDecimal bp1=new BigDecimal(p1);
						BigDecimal bp2=new BigDecimal(p2);
						Double animalProfit=bp2.subtract(bp1).doubleValue();
						profits.setAnimalProfit(animalProfit);
						User record = userDao.queryById(sellerId);//卖家
						String inviteCode = record.getInvitedCode();//卖家被邀请的码,分享者的邀请码
						if(!StringUtils.isEmpty(inviteCode)) {
							User sharer=userDao.queryByInviteCode(inviteCode);
							profits.setSharerId(sharer.getId());
							//计算分享金额
							List<User> list5 = userDao.queryByInvitedCode(inviteCode);//分享者的下线
							int size = list5.size();
							Double d=0D;
							if(size<10) {
								d=1D;
							}else if(size>9 && size<20 ) {
								d=1.2D;
							}else if(size>19) {
								d=1.5D;
							}
							BigDecimal bd2=new BigDecimal(d);
							Double shareProfit=bp2.multiply(bd2).divide(new BigDecimal(100D), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
							profits.setShareProfit(shareProfit);
						}
						//添加利润信息
						profitsService.add(profits);
					}
				}
			}
			System.out.println("自动确认执行结束");
		}
		 catch (Exception e) {
			 System.out.println("自动确认出错");
		}
	}
}

