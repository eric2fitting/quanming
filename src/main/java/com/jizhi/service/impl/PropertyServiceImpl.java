package com.jizhi.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jizhi.dao.PropertyDao;
import com.jizhi.pojo.Animal;
import com.jizhi.pojo.Match;
import com.jizhi.pojo.Property;
import com.jizhi.pojo.vo.FeedingDetail;
import com.jizhi.service.AnimalService;
import com.jizhi.service.MatchService;
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
	@Override
	public List<Property> queryCanSell(HashMap<String, Object> map) {
		return propertyDao.queryCanSell(map);
	}
	@Override
	public void updateState(Integer id) {
		propertyDao.updateState(id);
		
	}
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
	 * 转让中的动物列表
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
				//根据propertyId去匹配表中查询卖双方都没有确认的信息
				HashMap<String, Object> hashMap=new HashMap<String, Object>();
				hashMap.put("propertyId", propertyId);
				hashMap.put("buyerConfirm", 1);
				hashMap.put("sellerConfirm", 0);
				Match match= matchService.queryByPropertyId(hashMap);
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
}
