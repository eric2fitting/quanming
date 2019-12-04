package com.jizhi.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jizhi.pojo.OrderTime;

@Mapper
public interface OrderTimeDao {

	List<OrderTime> queryByAnimalId(Integer id);

	List<String> queryStartTimeByAnimalId(Integer animalId);

	String queryLastTime(HashMap<String, Object> map);

}
