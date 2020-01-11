package com.jizhi.dao;

import org.apache.ibatis.annotations.Mapper;

import com.jizhi.pojo.OtherInfo;

@Mapper
public interface OtherInfoDao {

	OtherInfo query();

}
