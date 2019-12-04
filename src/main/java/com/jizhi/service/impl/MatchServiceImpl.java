package com.jizhi.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jizhi.dao.MatchDao;
import com.jizhi.dao.OrderTimeDao;
import com.jizhi.pojo.Animal;
import com.jizhi.pojo.Match;
import com.jizhi.pojo.Order;
import com.jizhi.pojo.Property;
import com.jizhi.pojo.vo.BuyingDetail;
import com.jizhi.service.AnimalService;
import com.jizhi.service.MatchService;
import com.jizhi.service.OrderService;
import com.jizhi.service.PropertyService;
import com.jizhi.util.RedisService;

@Service
public class MatchServiceImpl implements MatchService{
	@Autowired
	private AnimalService animalService;
	@Autowired
	private MatchDao matchDao;
	@Autowired
	private OrderService orderService;
	@Autowired
	private PropertyService propertyService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private OrderTimeDao orderTimeDao;

	//一对一匹配，写入数据库
	public void doMatch(Order order, Property property) {
		//预约id
		Integer orderId = order.getId();
		//资产id
		Integer propertyId = property.getId();
		//根据动物id查找利润率
		Integer animalId = order.getAnimalId();
		Animal animal=animalService.queryById(animalId);
		Integer profit = animal.getProfit();
		//动物的进价
		Double price = property.getPrice();
		//计算卖价，保留两位小数
		Double sellPrice=price*(1+profit/100);
		BigDecimal bigDecimal = new BigDecimal(sellPrice);
		sellPrice=bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		//往配对表中添加数据
		Match match = new Match();
		match.setOrderId(orderId);
		match.setPropertyId(propertyId);
		match.setPrice(price);
		add(match);
		//添加成功后更改订单表和资产表状态
		orderService.updateState(orderId);
		propertyService.updateState(propertyId);	
		//TODO 给双方发送推送消息
		}
	@Override
	public Integer add(Match match) {
		return matchDao.add(match);
	}
	
	
	/**
	 * 领养中：详情列表
	 */
	@Override
	public List<BuyingDetail> showAllBuying(String token) {
		String string = redisService.get(token);
		//用户id,现在的身份是买家
		Integer buyerId=Integer.parseInt(string);
		//根据用户id从预约表中查找预约成功的预约id
		List<Order> orders=orderService.querySuccessOrder(buyerId);
		List<Match> matches=new ArrayList<Match>();
		//根据预约表的id查找match表的信息
		for(Order order:orders) {
			Match match=matchDao.queryByOrderId(order.getId());
			matches.add(match);
		}
		//用于封装领养信息
		ArrayList<BuyingDetail> list = new ArrayList<BuyingDetail>();
		for(Match match:matches) {
			//资产表的id
			Integer propertyId = match.getPropertyId();
			//得到资产信息也就是卖家该条信息
			Property property=propertyService.queryById(propertyId);
			//得到动物类型及所有信息
			Integer animalId = property.getAnimalId();
			Animal animal = animalService.queryById(animalId);
			//将所有需要展示额信息封装到buyingDetail中
			BuyingDetail buyingDetail = new BuyingDetail();
			buyingDetail.setMatchId(match.getId());
			buyingDetail.setPrice(match.getPrice());
			buyingDetail.setIsPaid(match.getBuyerConfirm());
			buyingDetail.setAnimalType(animal.getAnimalType());
			buyingDetail.setSize(animal.getSize());
			buyingDetail.setCycleProfit(animal.getCycle()+"/"+animal.getProfit());
			//计算可出售的日期、时间
			Calendar instance = Calendar.getInstance();
			instance.add(Calendar.DATE, animal.getCycle());
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String sellDate = simpleDateFormat.format(instance.getTime());//可出售的日期
			String sellTime=sellDate+" "+property.getBuyTime();
			buyingDetail.setSellTime(sellTime);
			//计算最后的付款日期、时间
			String nowDate = simpleDateFormat.format(new Date());
			HashMap<String, Object> map = new HashMap<String,Object>();
			map.put("startTime", property.getBuyTime());
			map.put("animalId", animalId);
			String lastTime=orderTimeDao.queryLastTime(map);
			buyingDetail.setLastPayTime(nowDate+lastTime);
			list.add(buyingDetail);
		}
		return list;
	}
	
	
	@Override
	public Match queryByOrderId(Integer id) {
		return matchDao.queryByOrderId(id);
	}
		
		
}
