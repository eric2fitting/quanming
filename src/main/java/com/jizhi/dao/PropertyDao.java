package com.jizhi.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jizhi.pojo.Property;

@Mapper
public interface PropertyDao {

	List<Property> queryCanSell(HashMap<String, Object> map);


	void updateState(Integer id);

	Double queryTotalMonet(int userId);


	Property queryById(Integer id);


	List<Property> queryByUserId(Integer userId);


	List<Property> queryIsSelling(HashMap<String, Object> map);

	

}
