package com.jizhi.service;

import java.util.HashMap;
import java.util.List;



import com.jizhi.pojo.Property;

public interface PropertyService {
	//查询可售的动物
	List<Property> queryCanSell(HashMap<String,Object> map);

	void updateState(Integer id);

	Double queryTotalMonet(int id);

	Property queryById(Integer id);

}
