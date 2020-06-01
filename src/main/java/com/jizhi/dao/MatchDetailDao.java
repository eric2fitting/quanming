package com.jizhi.dao;

import org.apache.ibatis.annotations.Mapper;

import com.jizhi.pojo.MatchDetail;

@Mapper
public interface MatchDetailDao {
	
	MatchDetail queryByAnimalId(Integer animalId);

}
