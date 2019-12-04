package com.jizhi.service;

import java.util.List;

import com.jizhi.pojo.Animal;
import com.jizhi.pojo.vo.ShowInfo;

public interface AnimalService {

	List<ShowInfo> queryAnimalList();

	List<Animal> queryByStartTime(String startTime);

	Animal queryById(Integer animalId);

}
