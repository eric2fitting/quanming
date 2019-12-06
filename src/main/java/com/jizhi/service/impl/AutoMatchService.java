package com.jizhi.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jizhi.pojo.Animal;
import com.jizhi.pojo.Order;
import com.jizhi.pojo.OrderTime;
import com.jizhi.pojo.Property;
import com.jizhi.pojo.vo.ShowInfo;
import com.jizhi.service.AnimalService;
import com.jizhi.service.MatchService;
import com.jizhi.service.OrderService;
import com.jizhi.service.PropertyService;

@Component
@Configuration
@EnableScheduling
public class AutoMatchService {
	@Autowired
	private AnimalService animalService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private PropertyService propertyService;
	
	@Autowired
	private MatchService matchService;
	
	/**
	 * 自动匹配预约用户和资产拥有者
	 */
	@Scheduled(fixedRate=60*1000*60)
	public void match() {
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm");
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		//封装所有开始时间段
		List<Date> startTimes = new ArrayList<Date>();
		try {
			//现在的时间
			Date nowDate=simpleDateFormat1.parse(simpleDateFormat1.format(new Date()));
			//首页展示所有动物时间等信息
			List<ShowInfo> list = animalService.queryAnimalList();
			for(ShowInfo showInfo:list) {
				//一种动物对应的所有的预约领养时间段
				List<OrderTime> list2 = showInfo.getList();
				for(OrderTime orderTime:list2) {
					Date startDate = simpleDateFormat1.parse(orderTime.getStartTime());
					if(!startTimes.contains(startDate)) {
						startTimes.add(startDate);
					}
				}
			}
			//从list中取出即将到的最近时间
			int nearesti=0;
			for(int i=1;i<list.size();i++ ) {
				if(startTimes.get(nearesti).after(nowDate)&&startTimes.get(i).after(nowDate)) {
					if(startTimes.get(nearesti).after(startTimes.get(i))) {
						nearesti=i;
					}
				}else if (startTimes.get(nearesti).before(nowDate)&&startTimes.get(i).after(nowDate)) {
					nearesti=i;
				}
			}
			//TODO 如果
			long sleepTime=(startTimes.get(nearesti).getTime()-nowDate.getTime());
			Thread.sleep(sleepTime);
			//开始自动匹配
			//根据开始匹配时间查询可以预约的动物
			String time = simpleDateFormat1.format(startTimes.get(nearesti));//当前时间
			List<Animal> animals=animalService.queryByStartTime(time);
			//根据动物id查询所有预约信息
			for(Animal animal:animals ) {
				Integer animalId = animal.getId();
				//喂养收益周期
				Integer cycle = animal.getCycle();
				//当前日期
				String date=simpleDateFormat2.format(nowDate);
				//根据动物id、预约日期、时间查询预约单
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("animalId", animalId);
				map.put("time", time);
				map.put("date", date);
				List<Order> orders=this.orderService.queryAll(map);
				//根据动物id，当前时间查询可售的资产表
				Calendar instance = Calendar.getInstance();
				instance.add(Calendar.DATE, -cycle);
				String buyDate = simpleDateFormat2.format(instance.getTime());
				String buyTime=time;
				HashMap<String, Object> map1 = new HashMap<String, Object>();
				map1.put("animalId", animalId);
				map1.put("buyDate", buyDate);
				map1.put("buyTime", buyTime);
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
					//TODO 剩余的全部匹配给管理员
				}
			}
			
			
		} catch (Exception e) {
			
		}
		
	}
	
	
}
