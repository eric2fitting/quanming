package com.jizhi.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jizhi.pojo.Property;

@Mapper
public interface PropertyDao {

	List<Property> queryCanSell(HashMap<String, Object> map);


	void updateState(Property property);

	Double queryTotalMonet(int userId);


	Property queryById(Integer id);


	List<Property> queryByUserId(Integer userId);


	List<Property> queryIsSelling(HashMap<String, Object> map);


	void add(Property property);
	
	//更改为已售
	void updateToSold(Integer id);

	

}
