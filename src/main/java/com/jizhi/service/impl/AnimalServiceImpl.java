package com.jizhi.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jizhi.dao.AnimalDao;
import com.jizhi.dao.OrderTimeDao;
import com.jizhi.pojo.Animal;
import com.jizhi.pojo.OrderTime;
import com.jizhi.pojo.vo.ShowInfo;
import com.jizhi.service.AnimalService;

@Service
public class AnimalServiceImpl implements AnimalService{
	@Autowired
	private AnimalDao animalDao;
	@Autowired
	private OrderTimeDao orderTimeDao;
	
	@Override
	public List<ShowInfo> queryAnimalList() {
		//查询所有动物列表
		List<Animal> list1=this.animalDao.queryAll();
		//将所有动物和时间段消息封装到result中返回
		ArrayList<ShowInfo> result = new ArrayList<ShowInfo>();
		for(Animal animal:list1) {
			//根据animalId查询该动物的所有预约时间段
			List<OrderTime> list2=this.orderTimeDao.queryByAnimalId(animal.getId());
			ShowInfo showInfo = new ShowInfo();
			showInfo.setAnimal(animal);
			showInfo.setList(list2);
			result.add(showInfo);
		}
		return result;
	}
	
	//根据开始预约时间查询可预约的动物

	@Override
	public List<Animal> queryByStartTime(String startTime) {
		ArrayList<Animal> animals = new ArrayList<Animal>();
		List<OrderTime> orderTimes=orderTimeDao.queryByStartTime(startTime);
		for(OrderTime orderTime:orderTimes) {
			Integer animalId = orderTime.getAnimalId();
			Animal animal = animalDao.queryById(animalId);
			animals.add(animal);
		}
		return animals;
	}
	
	//根据动物id查询动物的信息
	@Override
	public Animal queryById(Integer animalId) {
		return animalDao.queryById(animalId);
	}

	@Override
	public List<Animal> queryByEndTime(String endTime) {
		ArrayList<Animal> animals = new ArrayList<Animal>();
		List<OrderTime> orderTimes=orderTimeDao.queryByEndTime(endTime);
		for(OrderTime orderTime:orderTimes) {
			Integer animalId = orderTime.getAnimalId();
			Animal animal = animalDao.queryById(animalId);
			animals.add(animal);
		}
		return animals;
	}
	
	/**
	 * 根据动物类型大小查找动物id
	 */
	@Override
	public Integer queryAnimalId(HashMap<String, Object> map) {
		return animalDao.queryAnimalId(map);
	}
	

}
