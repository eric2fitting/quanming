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
import com.jizhi.pojo.vo.FeedingDetail;
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
		//用于封装领养信息
		ArrayList<BuyingDetail> list = new ArrayList<BuyingDetail>();
		if(orders==null) {
			list=null;
		}else {
			//根据预约表的id查找match表的信息
			List<Match> matches=new ArrayList<Match>();
			for(Order order:orders) {
				Match match=matchDao.queryByOrderId(order.getId());
				matches.add(match);
			}
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
				buyingDetail.setCycleProfit(animal.getCycle()+"天/"+animal.getProfit()+"%");
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
		}
		return list;
	}
	
	/**
	 * 喂养中详情列表
	 */
	@Override
	public List<FeedingDetail> showAllFeeding(String token) {
		try {
			//从redis中得到用户id
			String string = redisService.get(token);
			Integer userId=Integer.parseInt(string);
			//根据userId从资产表中查询
			List<Property> Properties=propertyService.queryByUserId(userId);
			//用于封装喂养的动物详情
			ArrayList<FeedingDetail> list = new ArrayList<FeedingDetail>();
			if(Properties==null) {
				list=null;
			}else {
				for(Property property:Properties) {
					Integer animalId = property.getAnimalId();
					//根据animalId查询该动物的信息
					Animal animal = animalService.queryById(animalId);
					FeedingDetail feedingDetail = new FeedingDetail();
					feedingDetail.setId(property.getId());
					feedingDetail.setBuyTime(property.getBuyDate());
					feedingDetail.setAnimalType(animal.getAnimalType());
					feedingDetail.setSize(animal.getSize());
					Integer proftis=animal.getProfit()+95;//利润率
					feedingDetail.setPrice(property.getPrice()+"×"+proftis+"%");
					feedingDetail.setCycleProfit(animal.getCycle()+"天/"+(animal.getProfit()-5)+"%");
					feedingDetail.setProfit("价值*"+animal.getProfit());
					//转让出栏时间
					String buyTime = property.getBuyTime();//买入时间
					Integer days=animal.getCycle();//喂养天数
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Date buyDate = simpleDateFormat.parse(buyTime);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(buyDate);
					calendar.add(Calendar.DATE, days);
					String sellTime = simpleDateFormat.format(calendar.getTime());
					feedingDetail.setSellTime(sellTime);
					if(property.getIsSold()==0) {
						feedingDetail.setState("喂养收益中");
					}else if (property.getIsSold()==1) {
						feedingDetail.setState("正在转让中");
					}
					list.add(feedingDetail);
				}
				return list;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	
	/**
	 * 根据预约id查询匹配信息
	 */
	@Override
	public Match queryByOrderId(Integer id) {
		return matchDao.queryByOrderId(id);
	}
	
	/**
	 * 根据资产id查找匹配信息
	 */
	@Override
	public Match queryByPropertyId(HashMap<String, Object> map) {
		return matchDao.queryByPropertyId(map);
	}

		
		
}
