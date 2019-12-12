package com.jizhi.dao;

import org.apache.ibatis.annotations.Mapper;

import com.jizhi.pojo.FirstPage;

@Mapper
public interface FirstPageDao {

	FirstPage query();

}
