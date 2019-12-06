package com.jizhi.service;

import java.util.List;

import com.jizhi.pojo.Animal;
import com.jizhi.pojo.vo.ShowInfo;

public interface AnimalService {
	//查找首页所有需要展示的信息。包含动物信息及预约时间
	List<ShowInfo> queryAnimalList();
	//根据开始预约的时间查询所有动物列表
	List<Animal> queryByStartTime(String startTime);
	//根据动物id查询所有信息
	Animal queryById(Integer animalId);

}
