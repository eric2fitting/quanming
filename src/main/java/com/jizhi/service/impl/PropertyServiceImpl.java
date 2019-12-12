package com.jizhi.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jizhi.dao.MatchDao;
import com.jizhi.dao.PropertyDao;
import com.jizhi.dao.UserDao;
import com.jizhi.pojo.Animal;
import com.jizhi.pojo.Match;
import com.jizhi.pojo.Order;
import com.jizhi.pojo.Profits;
import com.jizhi.pojo.Property;
import com.jizhi.pojo.User;
import com.jizhi.pojo.vo.FeedingDetail;
import com.jizhi.pojo.vo.SellInfo;
import com.jizhi.service.AnimalService;
import com.jizhi.service.MatchService;
import com.jizhi.service.OrderService;
import com.jizhi.service.ProfitsService;
import com.jizhi.service.PropertyService;
import com.jizhi.util.RedisService;
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
		if(Properties==null) {
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
					feedingDetail.setId(match.getId());
					feedingDetail.setAnimalType(animal.getAnimalType());
					feedingDetail.setSize(animal.getSize());
					feedingDetail.setBuyTime(property.getBuyDate());
					feedingDetail.setPrice(match.getPrice()+"");
					feedingDetail.setProfit(animal.getCycle()+"天/"+(animal.getProfit()-5)+"%");
					feedingDetail.setCycleProfit(animal.getCycle()+"天/"+(animal.getProfit()-5)+"%");
					Date date = new Date();
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
					String sellTime=simpleDateFormat.format(date);
					feedingDetail.setSellTime(sellTime);
					feedingDetail.setState("已出栏");
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
		//用于封装转让中的列表信息
		ArrayList<FeedingDetail> list = new ArrayList<FeedingDetail>();
		//从资产表中查找正在卖的资产列表
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("isSold", 1);//1表示正在出售
		List<Property> Properties=propertyDao.queryIsSelling(map);
		if(Properties==null) {
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
					feedingDetail.setId(match.getId());//订单编号
					feedingDetail.setAnimalType(animal.getAnimalType());//物种
					feedingDetail.setSize(animal.getSize());//大小
					feedingDetail.setBuyTime(property.getBuyDate());//领养时间
					feedingDetail.setPrice(match.getPrice()+"");//价值
					feedingDetail.setProfit(animal.getCycle()+"天/"+(animal.getProfit()-5)+"%");//只能喂养合约收益
					feedingDetail.setCycleProfit("价值*"+animal.getProfit()+"%");//喂养出栏收益
					feedingDetail.setNoteMsg("平台扣除5%转让手续费");//
					feedingDetail.setState("等待领养中");
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
		if(Properties==null) {
			list=null;
		}else {
			//从匹配表中查找已匹配好的
			for(Property property:Properties) {
				Integer animalId = property.getAnimalId();
				Animal animal = animalService.queryById(animalId);
				FeedingDetail feedingDetail = new FeedingDetail();
				feedingDetail.setId(property.getId());
				feedingDetail.setBuyTime(property.getBuyDate());
				feedingDetail.setAnimalType(animal.getAnimalType());
				feedingDetail.setSize(animal.getSize());
				feedingDetail.setPrice(property.getPrice()+"×"+(animal.getProfit()-5)+"%");
				feedingDetail.setCycleProfit(animal.getCycle()+"天/"+(animal.getProfit()-5)+"%");
				feedingDetail.setState("已完成转让收益");
				list.add(feedingDetail);
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
		sellInfo.setPrice(match.getPrice());
		sellInfo.setPic(match.getPayPic());
		return sellInfo;
	}
	@Override
	public void doSell(Integer matchId) {
		//更改匹配表里卖家确认
		matchDao.updateSellerConfirm(matchId);
		//从匹配表里得到预约信息
		Match match = matchDao.queryById(matchId);
		//将资产状态变为已售
		Integer pId=match.getPropertyId();
		updateToSold(pId);
		
		Integer orderId = match.getOrderId();
		Order order=orderService.queryByOrderId(orderId);
		Integer buyerId=order.getUserId();
		//判断该买家是否是第一次购买成功，若是，更改状态为活跃状态
		List<Property> Properties = propertyDao.queryByUserId(buyerId);
		if(Properties.size()==0) {
			//此次是第一次购买,将状态变为活跃
			userDao.updateState(buyerId);
		}
		//往资产表里添加新数据，买家买入后在资产表里生成自己的新资产
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Property property = new Property();
		property.setAnimalId(order.getAnimalId());
		property.setBuyDate(simpleDateFormat.format(new Date()));
		property.setBuyTime(order.getTime());
		property.setIsSold(0);
		Double price = match.getPrice();
		property.setPrice(price);
		property.setUserId(buyerId);
		add(property);
		//从匹配表里查询资产表信息
		Integer propertyId = match.getPropertyId();
		Property property1 = propertyDao.queryById(propertyId);
		Integer animalId = property1.getAnimalId();
		//根据动物id查询动物信息
		Animal animal = animalService.queryById(animalId);
		Double price1 = property1.getPrice();
		BigDecimal bigDecimal = new BigDecimal(price);
		BigDecimal bigDecimal1=new BigDecimal(price1);
		Double sellerprofit = bigDecimal.subtract(bigDecimal1).doubleValue();
		//往利润表里添加卖家的喂养收益和邀请卖家的用户应得到的分享收益
		Profits profits = new Profits();
		profits.setAnimalProfit(sellerprofit);
		profits.setNFC(animal.getNfc());
		Integer userId = property1.getUserId();
		profits.setUserId(userId);
		User user = userDao.queryById(userId);//卖家
		String inviteCode = user.getInvitedCode();//卖家被邀请的码,分享者的邀请码
		if(!StringUtils.isEmpty(inviteCode)) {
			User sharer=userDao.queryByInviteCode(inviteCode);
			profits.setSharerId(sharer.getId());
			//计算分享金额
			List<User> list = userDao.queryByInvitedCode(inviteCode);//分享者一共分享的用户
			int size = list.size();
			Double d=0D;
			if(size<10) {
				d=1D;
			}else if(size>9 &&size<20 ) {
				d=1.2D;
			}else if(size>19) {
				d=1.5D;
			}
			BigDecimal bigDecimal2=new BigDecimal(d);
			BigDecimal bigDecimal3=new BigDecimal(100D);
			Double shareProfit=bigDecimal.multiply(bigDecimal2).divide(bigDecimal3, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
			profits.setShareProfit(shareProfit);
		}
		profitsService.add(profits);
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
	
	
}
