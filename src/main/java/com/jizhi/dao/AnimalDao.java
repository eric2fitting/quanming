package com.jizhi.dao;

import com.jizhi.pojo.Animal;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface AnimalDao {
	List<Animal> showAnimalList();

	List<Animal> queryAll();

	Animal queryById(Integer animalId);

	Integer queryAnimalId(HashMap<String, Object> map);

}
