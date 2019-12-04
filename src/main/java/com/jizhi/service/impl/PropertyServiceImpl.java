package com.jizhi.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jizhi.dao.PropertyDao;
import com.jizhi.pojo.Property;
import com.jizhi.service.PropertyService;
@Service
public class PropertyServiceImpl implements PropertyService{
	
	@Autowired
	private PropertyDao propertyDao;
	@Override
	public List<Property> queryCanSell(HashMap<String, Object> map) {
		return propertyDao.queryCanSell(map);
	}
	@Override
	public void updateState(Integer id) {
		propertyDao.updateState(id);
		
	}
	@Override
	public Double queryTotalMonet(int id) {
		return propertyDao.queryTotalMonet(id);
	}
	@Override
	public Property queryById(Integer id) {
		return propertyDao.queryById(id);
	}

}
