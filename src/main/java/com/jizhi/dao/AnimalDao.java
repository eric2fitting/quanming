package com.jizhi.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jizhi.pojo.Animal;

@Mapper
public interface AnimalDao {
	List<Animal> showAnimalList();

	List<Animal> queryAll();

	List<Animal> queryByStartTime(String startTime);

	Animal queryById(Integer animalId);
}
