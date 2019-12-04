package com.jizhi.dao;

import org.apache.ibatis.annotations.Mapper;

import com.jizhi.pojo.IdCard;

@Mapper
public interface IdCardDao {

	int save(IdCard idCard);

	int update(IdCard idCard);

}
