package com.jizhi.dao;

import com.jizhi.pojo.Property;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

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


	List<Property> queryNotMatched(Integer userId);


	void updateBuyDateTime(HashMap<String, Object> map);

	@Delete("delete from property where id>200")
	Integer deleteAll();

	

}
