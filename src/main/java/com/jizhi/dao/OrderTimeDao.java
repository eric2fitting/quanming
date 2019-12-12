package com.jizhi.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jizhi.pojo.OrderTime;

@Mapper
public interface OrderTimeDao {

	List<OrderTime> queryByAnimalId(Integer id);


	String queryLastTime(HashMap<String, Object> map);


	OrderTime queryById(Integer id);


	List<OrderTime> queryByStartTime(String startTime);
	
	List<OrderTime> queryByEndTime(String endTime);

}
