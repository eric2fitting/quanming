package com.jizhi.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jizhi.dao.FeedDao;
import com.jizhi.dao.MatchDao;
import com.jizhi.dao.OrderDao;
import com.jizhi.dao.OrderTimeDao;
import com.jizhi.dao.PropertyDao;
import com.jizhi.dao.UserDao;
import com.jizhi.pojo.Animal;
import com.jizhi.pojo.Feed;
import com.jizhi.pojo.Match;
import com.jizhi.pojo.Order;
import com.jizhi.pojo.Profits;
import com.jizhi.pojo.Property;
import com.jizhi.pojo.User;
import com.jizhi.pojo.vo.FeedingDetail;
import com.jizhi.pojo.vo.Sell;
import com.jizhi.pojo.vo.SellInfo;
import com.jizhi.service.AnimalService;
import com.jizhi.service.MatchService;
import com.jizhi.service.OrderService;
import com.jizhi.service.ProfitsService;
import com.jizhi.service.PropertyService;
import com.jizhi.service.UserSevice;
import com.jizhi.util.RedisService;
@Transactional(rollbackFor = Exception.class)
@Service
public class PropertyServiceImpl implements PropertyService{
	
	@Autowired
	private PropertyDao propertyDao;
	@Autowired
	private RedisService redisService;
	@Autowired
	private MatchService matchService;
	@Autowired
	private AnimalService animalService;
	@Autowired
	private MatchDao matchDao;
	@Autowired
	private OrderService orderService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ProfitsService profitsService;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private FeedDao feedDao;
	private final SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
	
	@Autowired
	private OrderTimeDao orderTimeDao;
	@Autowired
	private UserSevice userSevice;
	
	private static final Logger log = LoggerFactory.getLogger(PropertyServiceImpl.class);
	
	@Override
	public List<Property> queryCanSell(HashMap<String, Object> map) {
		return propertyDao.queryCanSell(map);
	}
	
//	@Override
//	public void updateState(Property property) { 
//		propertyDao.updateState(property);
//	  
//	}
	 
	@Override
	public Double queryTotalMonet(Integer id) {
		return propertyDao.queryTotalMonet(id);
	}
	@Override
	public Property queryById(Integer id) {
		return propertyDao.queryById(id);
	}
	@Override
	public List<Property> queryByUserId(Integer userId) {
		
		return propertyDao.queryByUserId(userId);
	}
	
	/**
	 * 等待转让的动物列表
	 */
	@Override
	public List<FeedingDetail> queryWaitSellList(String token) {
		String string = redisService.get(token);
		Integer userId = Integer.parseInt(string);
		//用于封装转让中的列表信息
		ArrayList<FeedingDetail> list = new ArrayList<FeedingDetail>();
		//从资产表中查找正在卖的资产列表
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("isSold", 1);//1表示正在出售
		List<Property> Properties=propertyDao.queryIsSelling(map);
		if(Properties.size()==0) {
			list=null;
		}else {
			//从匹配表中查找已匹配好的
			for(Property property:Properties) {
				Integer animalId = property.getAnimalId();
				Animal animal = animalService.queryById(animalId);
				Integer propertyId = property.getId();
				//根据propertyId去匹配表中查询买卖双方都没有确认的信息
				HashMap<String, Object> hashMap=new HashMap<String, Object>();
				hashMap.put("propertyId", propertyId);
				hashMap.put("buyerConfirm", 0);
				hashMap.put("sellerConfirm", 0);
				Match match= matchService.queryByPropertyId(hashMap);
				if(match!=null) {
					FeedingDetail feedingDetail = new FeedingDetail();
					feedingDetail.setId(match.getId());//匹配表id
					feedingDetail.setNumber(property.getCode());//区块编码
					feedingDetail.setAnimalType(animal.getAnimalType());//种类
					feedingDetail.setSize(animal.getSize());//大小型号
					feedingDetail.setPrice(property.getPrice()+"");//价值
					feedingDetail.setCycleProfit(animal.getCycle()+"天/"+(animal.getProfit())+"%"+"  "+match.getPrice());//智能收益
					HashMap<String, Object> hashMap2 = new HashMap<String, Object>();
					hashMap2.put("animalId", animalId);
					hashMap2.put("startTime", property.getBuyTime());
					String endTime = orderTimeDao.queryLastTime(hashMap2);
					feedingDetail.setBuyTime(property.getBuyDate()+"  "+property.getBuyTime()+"-"+endTime);//领养时间
					Date date = new Date();
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
					String sellTime=simpleDateFormat.format(date);
					feedingDetail.setSellTime(sellTime+"  "+property.getBuyTime()+"-"+endTime);//转让时间
					feedingDetail.setState("出栏中");//状态
					list.add(feedingDetail);
				}
			}
		}
		
		return list;
	}
	/**
	 * 正在转让的列表
	 */
	@Override
	public List<FeedingDetail> queryIsSellingList(String token) {
		String string = redisService.get(token);
		Integer userId = Integer.parseInt(string);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = simpleDateFormat.format(new Date());
		//用于封装转让中的列表信息
		ArrayList<FeedingDetail> list = new ArrayList<FeedingDetail>();
		//从资产表中查找正在卖的资产列表
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("isSold", 1);//1表示正在出售
		List<Property> Properties=propertyDao.queryIsSelling(map);
		if(Properties.size()==0) {
			list=null;
		}else {
			//从匹配表中查找已匹配好的
			for(Property property:Properties) {
				Integer animalId = property.getAnimalId();
				Animal animal = animalService.queryById(animalId);
				Integer propertyId = property.getId();
				//根据propertyId去匹配表中查看自己没有确认而对方确认的信息
				HashMap<String, Object> hashMap=new HashMap<String, Object>();
				hashMap.put("propertyId", propertyId);
				hashMap.put("buyerConfirm", 1);
				hashMap.put("sellerConfirm", 0);
				//从匹配表中得到买家确认了自己还没确认的匹配信息
				Match match= matchService.queryByPropertyId(hashMap);
				if(match!=null) {
					FeedingDetail feedingDetail = new FeedingDetail();
					feedingDetail.setId(match.getId());//匹配表id
					feedingDetail.setNumber(property.getCode());//区块编码
					feedingDetail.setAnimalType(animal.getAnimalType());//物种
					feedingDetail.setSize(animal.getSize());//大小
					feedingDetail.setPrice(property.getPrice()+"");//价值,曾经的买价
					HashMap<String,Object> map1=new HashMap<String,Object>();
					map1.put("animalId",animalId);
					map1.put("startTime",property.getBuyTime());
					String endTime = orderTimeDao.queryLastTime(map1);
					feedingDetail.setBuyTime(property.getBuyDate()+"  "+property.getBuyTime()+"-"+endTime);//领养时间
					feedingDetail.setSellTime(nowDate+"  "+property.getBuyTime()+"-"+endTime);//转让时间
					feedingDetail.setLastTime(nowDate+"  "+endTime);//最后确认时间
					feedingDetail.setCycleProfit(animal.getCycle()+"天/"+(animal.getProfit())+"%"+"  "+match.getPrice());//智能收益
					feedingDetail.setState("核对支付凭证");
					list.add(feedingDetail);
				}
			}
		}
		return list;
	}
	
	/**
	 * 已经完成转让的动物列表
	 */
	@Override
	public List<FeedingDetail> queryIsSoldList(String token) {
		String string = redisService.get(token);
		Integer userId = Integer.parseInt(string);
		//用于封装转让中的列表信息
		ArrayList<FeedingDetail> list = new ArrayList<FeedingDetail>();
		//从资产表中查找正在卖的资产列表
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("isSold", 2);//1表示正在出售
		List<Property> Properties=propertyDao.queryIsSelling(map);
		if(Properties.size()==0) {
			list=null;
		}else {
			//从匹配表中查找已匹配好的
			for(Property property:Properties) {
				try {
					Integer animalId = property.getAnimalId();
					Animal animal = animalService.queryById(animalId);
					HashMap<String,Object> map1=new HashMap<String,Object>();
					map1.put("animalId",animalId);
					map1.put("startTime",property.getBuyTime());
					String endTime = orderTimeDao.queryLastTime(map1);//最后时间
					FeedingDetail feedingDetail = new FeedingDetail();
					feedingDetail.setNumber(property.getCode());//区块编码
					feedingDetail.setBuyTime(property.getBuyDate()+"  "+property.getBuyTime()+"-"+endTime);//领养时间
					feedingDetail.setAnimalType(animal.getAnimalType());//种类
					feedingDetail.setSize(animal.getSize());//大小
					Match match = matchDao.queryOnlyByPropertyId(property.getId());			
					feedingDetail.setPrice(property.getPrice()+"");//价值
					feedingDetail.setCycleProfit(animal.getCycle()+"天/"+(animal.getProfit())+"%"+"  "+match.getPrice());//智能收益
					feedingDetail.setState("已完成收益");
					//查询转让的时间
					Integer orderId=match.getOrderId();
					Order order = orderService.queryByOrderId(orderId);
					String date = order.getDate();
					feedingDetail.setSellTime(date+"  "+property.getBuyTime()+"-"+endTime);//转让时间
					feedingDetail.setId(match.getId());
					list.add(feedingDetail);
				} catch (Exception e) {
					log.info("匹配表里缺数据");
				}
			}
		}
		return list;
	}
	@Override
	public SellInfo toSell(Integer id) {
		SellInfo sellInfo = new SellInfo();
		Match match = matchDao.queryById(id);
		Integer orderId = match.getOrderId();
		Order order=orderService.queryByOrderId(orderId);
		Integer userId = order.getUserId();
		User user = userDao.queryById(userId);
		sellInfo.setBuyerName(user.getUserName());
		sellInfo.setBuyerTel(user.getTel());
		sellInfo.setPrice(matchService.calculateUsdtPrice(match.getPrice()));
		sellInfo.setUsdtPrice(match.getPrice());//usdt金额
		sellInfo.setPic(match.getPayPic());
		return sellInfo;
	}
	@Override
	public Integer doSell(Sell sell) {
		Integer matchId = sell.getId();
		Match match = matchDao.queryById(matchId);//从匹配表里得到预约信息
		Integer propertyId=match.getPropertyId();
		Property properti = propertyDao.queryById(propertyId);
		Integer sellerId=properti.getUserId();
		User seller=userDao.queryById(sellerId);
		//二级密码正确
		if(seller.getSecondpsw().equals(sell.getSecondPsw())) {
			return doSellDirectly(matchId);
		}else {
			return 0;
		}	
	}
	
	public Integer doSellDirectly(Integer matchId) {
		log.info("开始确认交易");
		Long l1=System.currentTimeMillis();
		Match match = matchDao.queryById(matchId);//从匹配表里得到预约信息
		if(match.getSellerConfirm()==1) {
			return 1;
		}
		//更改匹配表里卖家确认
		matchDao.updateSellerConfirm(matchId);
		//将资产状态变为已售
		Integer pId=match.getPropertyId();
		Property oldP = propertyDao.queryById(pId);//卖的动物资产
		updateToSold(pId);
		Integer orderId = match.getOrderId();
		Order order=orderService.queryByOrderId(orderId);//预约信息
		Integer animalId=order.getAnimalId();
		Integer buyerId=order.getUserId();
		User buyer = userDao.queryById(buyerId);
		//判断该买家是否是第一次购买成功，若是，更改状态为活跃状态
		List<Property> Properties = propertyDao.queryByUserId(buyerId);
		if(Properties.size()==0) {
			//此次是第一次购买,将状态变为活跃
			userDao.updateState(buyerId);
			//并查看自己变为活跃后是否对推荐者及前人们的等级是否有影响
			updateOlderUserLevel(buyer);
		}
		//往资产表里添加新数据-买家买入后在资产表里生成自己的新资产
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Animal animal = animalService.queryById(animalId);
		//动物的下次出售的价格超过了该种类的最大值
		Double buyPrice = match.getPrice();
		Double maxPrice = animal.getMaxPrice();
		Integer profit = animal.getProfit();
		BigDecimal b1 = new BigDecimal(buyPrice);
		BigDecimal b2 = new BigDecimal(profit);
		BigDecimal b4 = new BigDecimal(100D);
		Integer newAnimalId=animalId;
		Double sellPrice=b1.multiply(b2.add(b4)).divide(b4, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
		if(sellPrice>maxPrice || sellPrice==maxPrice) {
			HashMap<String,Object> map1=new HashMap<String,Object>();
			//改变尺寸
			if(animal.getSize().equals("A")) {
				map1.put("size", "B");
				map1.put("animalType", animal.getAnimalType());
				newAnimalId=animalService.queryAnimalId(map1);
				if(newAnimalId==null) {
					newAnimalId=animalId;
				}
			}else if(animal.getSize().equals("B")) {
				map1.put("size", "C");
				map1.put("animalType", animal.getAnimalType());
				newAnimalId=animalService.queryAnimalId(map1);
				if(newAnimalId==null) {
					newAnimalId=animalId;
				}
			}
		}
		Property property3 = new Property();
		property3.setAnimalId(newAnimalId);
		property3.setBuyDate(simpleDateFormat.format(new Date()));
		property3.setBuyTime(order.getTime());
		property3.setIsSold(0);
		property3.setUserId(buyerId);
		property3.setPrice(buyPrice);
		property3.setRole(order.getRole());
		property3.setCode(oldP.getCode());
		add(property3);	//新增资产
		Property property1 = oldP;//从匹配表里查询资产表信息
		//Integer animalId = property1.getAnimalId();
		//根据动物id查询动物信息
		//Animal animal = animalService.queryById(animalId);
		
		Double price1 = property1.getPrice();
		BigDecimal bigDecimal = new BigDecimal(buyPrice);
		BigDecimal bigDecimal1=new BigDecimal(price1);
		Double sellerprofit = bigDecimal.subtract(bigDecimal1).doubleValue();
		
		//往利润表里添加卖家的喂养收益
		Profits profits = new Profits();
		profits.setAnimalProfit(sellerprofit);
		profits.setNFC(0D);
		Integer userId = property1.getUserId();
		profits.setUserId(userId);
		profits.setShareProfit(0D);
		profits.setSharerId(null);
		//保存卖家的收益
		profitsService.add(profits);
		//给买家的添加mtoken（NFC）收益，扣除金券（饲料）
		Profits buyerProfits=new Profits();
		buyerProfits.setUserId(buyerId);
		buyerProfits.setAnimalProfit(0D);
		buyerProfits.setShareProfit(0D);
		buyerProfits.setSharerId(null);
		BigDecimal b21=new BigDecimal(sellPrice);
		BigDecimal b22=new BigDecimal(buyPrice);
		//买入成功后，预计收益的百分之10的价格，作为mToken(NFC)收益和金券（饲料）扣除
		Double mToken=(b21.subtract(b22)).multiply(new BigDecimal(0.1D)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		buyerProfits.setNFC(mToken);
		profitsService.add(buyerProfits);
		//扣除买家的金券
		Feed subFeed=new Feed();
		subFeed.setDate(new Date());
		subFeed.setNum(-mToken);
		subFeed.setOtherId(null);
		subFeed.setUserId(buyerId);
		subFeed.setType(8);//M-token兑换
		feedDao.insert(subFeed);
		//提前计算买家出售时的收益
		Animal aa=animalService.queryById(newAnimalId);//买家买入的动物
		BigDecimal b02 = new BigDecimal(100);
		bigDecimal1=new BigDecimal(aa.getProfit());
		sellerprofit=bigDecimal.multiply(bigDecimal1).divide(b02).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		//User user = userDao.queryById(userId);//卖家
		String inviteCode = buyer.getInvitedCode();//买家被邀请的码,分享者的邀请码
		//直推用户
		if(!StringUtils.isEmpty(inviteCode)) {
			User sharer=userDao.queryByInviteCode(inviteCode);
			BigDecimal sellerprofit_b=new BigDecimal(sellerprofit);
			if(sharer!=null) {
				if(buyer.getLevel()<sharer.getLevel() || sharer.getLevel()<2 ) {
					if(haveOrderedToday(sharer.getId())) {
						Profits profits_1=new Profits();
						profits_1.setUserId(buyerId);
						profits_1.setNFC(0D);
						profits_1.setAnimalProfit(0D);
						profits_1.setSharerId(sharer.getId());
						BigDecimal b01 = new BigDecimal(6);
						Double shareProfit=sellerprofit_b.multiply(b01).divide(b02).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						profits_1.setShareProfit(shareProfit);
						profitsService.add(profits_1);
					}
				}
				//查看是否有二级用户
				String secondInviteCode=sharer.getInvitedCode();//分享者被邀请的码，别人的邀请码
				if(StringUtils.isNotEmpty(secondInviteCode)) {
					User secondSharer=userDao.queryByInviteCode(secondInviteCode);
					if(secondSharer!=null) {
						if(buyer.getLevel()<secondSharer.getLevel() || secondSharer.getLevel()<2) {
							if(haveOrderedToday(secondSharer.getId())) {
								Profits profits2=new Profits();
								profits2.setUserId(buyerId);
								profits2.setNFC(0D);
								profits2.setAnimalProfit(0D);
								profits2.setSharerId(secondSharer.getId());
								BigDecimal b03 = new BigDecimal(4);
								Double SecondShareProfit=sellerprofit_b.multiply(b03).divide(b02).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
								profits2.setShareProfit(SecondShareProfit);
								profitsService.add(profits2);
							}
						}
						//是否有三级用户
						String thirdInviteCode=secondSharer.getInvitedCode();
						if(StringUtils.isNotEmpty(thirdInviteCode)) {
							User thirdSharer=userDao.queryByInviteCode(thirdInviteCode);
							if(thirdSharer!=null) {
								if(buyer.getLevel()<thirdSharer.getLevel() || thirdSharer.getLevel()<2) {
									if(haveOrderedToday(thirdSharer.getId())) {
										Profits profits3=new Profits();
										profits3.setUserId(buyerId);
										profits3.setNFC(0D);
										profits3.setAnimalProfit(0D);
										profits3.setSharerId(thirdSharer.getId());
										BigDecimal b04 = new BigDecimal(2);
										Double thirdShareProfit=sellerprofit_b.multiply(b04).divide(b02).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
										profits3.setShareProfit(thirdShareProfit);
										profitsService.add(profits3);
									}
									
								}
								addAllSharerShareProfits(buyer, thirdSharer, sellerprofit_b, 4);
							}
						}
					}
				}
			}
			
		}
		log.info("确认交易完成，用时："+(System.currentTimeMillis()-l1)+"毫秒");
		return 1;
	}
	
	
	public void addAllSharerShareProfits(User buyer,User record,BigDecimal sellerprofit,int num) {
		String invitedCode = record.getInvitedCode();
		if(StringUtils.isNotEmpty(invitedCode)) {
			User sharer = userDao.queryByInviteCode(invitedCode);
			if(sharer!=null) {
				if(sharer.getLevel()>buyer.getLevel()) {
					if(haveOrderedToday(sharer.getId())) {
						Profits profits=new Profits();
						profits.setUserId(buyer.getId());
						profits.setNFC(0D);
						profits.setAnimalProfit(0D);
						profits.setSharerId(sharer.getId());
						BigDecimal b02 = new BigDecimal(100);
						switch (sharer.getLevel()) {
						case 1:
							if(num<=10) {
								BigDecimal b04 = new BigDecimal(1);
								Double shareProfit=sellerprofit.multiply(b04).divide(b02).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
								profits.setShareProfit(shareProfit);
								profitsService.add(profits);
							}
							num++;
							break;
						case 2:
							if(num<=20) {
								BigDecimal b04 = new BigDecimal(1);
								Double shareProfit=sellerprofit.multiply(b04).divide(b02).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
								profits.setShareProfit(shareProfit);
								profitsService.add(profits);
							}
							num++;
							break;
						case 3:
							BigDecimal b04 = new BigDecimal(1);
							Double shareProfit=sellerprofit.multiply(b04).divide(b02).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
							profits.setShareProfit(shareProfit);
							profitsService.add(profits);
							num++;
							break;
						case 4:
							BigDecimal b05 = new BigDecimal(2);
							Double shareProfit1=sellerprofit.multiply(b05).divide(b02).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
							profits.setShareProfit(shareProfit1);
							profitsService.add(profits);
							num++;
							break;
						}
					}	
				}
				addAllSharerShareProfits(buyer,sharer,sellerprofit,num);
			}
		}
	}
	
	/**
	 * 查看上一级是否今天有预约，有预约才可以得到分享收益
	 * @param userId
	 * @return
	 */
	public boolean haveOrderedToday(Integer userId) {
		Date date = new Date();
		String today = simpleDateFormat.format(date);
		List<Order> orders=orderDao.queryByUserIdAndDate(userId,today);
		if(orders.size()>0) {
			return true;
		}
		return false;
	}
	
	
	
	/**
	 * 查看上一级的条件看是否可以更改
	 * @param user
	 */
	public void updateOlderUserLevel(User user){
		String invitedCode = user.getInvitedCode();
		if(StringUtils.isNotBlank(invitedCode)) {
			User inviter1 = userDao.queryByInviteCode(invitedCode);
			if(inviter1!=null) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("activeNum", userSevice.queryActiveMum(inviter1, 0));
				map.put("level2Num", userSevice.queryLevel2Num(inviter1, 0));
				map.put("level3Num", userSevice.queryLevel3Num(inviter1, 0));
				List<User> list = userDao.queryByInvitedCode(invitedCode);
				//HashMap<String, Object> userLevelFactor = userSevice.queryUserLevelFactor(list, 0, 0, 0);
				map.put("user", inviter1);
				//判断该邀请者是否更升入下一等级并执行
				Double shareProfit = profitsService.getAllShare(inviter1.getId());
				map.put("allShareProfit", shareProfit);
				map.put("level1Num", list.size());
				userSevice.updateLevel(map);
				//递归
				updateOlderUserLevel(inviter1);
			}
		}
	}
	
	
	
	
	
	public void add(Property property) {
		propertyDao.add(property);
	}

	@Override
	public void updateToSold(Integer id) {
		propertyDao.updateToSold(id);
		
	}

	@Override
	public void updateState(Property property) {
		propertyDao.updateState(property);
		
	}

	@Override
	public List<Property> queryNotMatched(Integer userId) {
		return propertyDao.queryNotMatched(userId);
	}

	@Override
	public void updateBuyDateTime(HashMap<String, Object> map) {
		propertyDao.updateBuyDateTime(map);
		
	}
	
	/**
	 * 驳回
	 */
	@Override
	public Integer cancelSell(Sell sell) {
		Integer matchId = sell.getId();
		Match match = matchDao.queryById(matchId);//从匹配表里得到预约信息
		Integer propertyId=match.getPropertyId();
		Property properti = propertyDao.queryById(propertyId);
		Integer sellerId=properti.getUserId();
		User seller=userDao.queryById(sellerId);
		//二级密码正确
		if(seller.getSecondpsw().equals(sell.getSecondPsw())) {
			return matchService.cancelSell(matchId);
		}else {
			return 2;
		}
	}

	@Override
	public Integer killAll() {
		return propertyDao.deleteAll();
	}

}
