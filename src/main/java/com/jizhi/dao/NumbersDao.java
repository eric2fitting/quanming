package com.jizhi.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
@Mapper
public interface NumbersDao {
	@Select("select number from numbers")
	String queryNumber();
	@Update("update numbers set number=#{number}")
	int updateNumber(String number);
}
