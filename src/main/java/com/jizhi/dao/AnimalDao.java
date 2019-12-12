package com.jizhi.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jizhi.pojo.Animal;

@Mapper
public interface AnimalDao {
	List<Animal> showAnimalList();

	List<Animal> queryAll();

	Animal queryById(Integer animalId);

	Integer queryAnimalId(HashMap<String, Object> map);

}
