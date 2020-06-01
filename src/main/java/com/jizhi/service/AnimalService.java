package com.jizhi.service;

import com.jizhi.pojo.Animal;
import com.jizhi.pojo.vo.ShowInfo;

import java.util.HashMap;
import java.util.List;

public interface AnimalService {
	//查找首页所有需要展示的信息。包含动物信息及预约时间
	List<ShowInfo> queryAnimalList();
	//根据开始预约的时间查询所有动物列表
	List<Animal> queryByStartTime(String startTime);
	//根据动物id查询所有信息
	Animal queryById(Integer animalId);
	//根据预约的最后时间查询所有动物列表
	List<Animal> queryByEndTime(String endTime);
	//根据动物类型和大小查找动物id
	Integer queryAnimalId(HashMap<String, Object> map);
	//查询动物编码
	String queryNumber();
}
