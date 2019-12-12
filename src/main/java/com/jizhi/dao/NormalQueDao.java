package com.jizhi.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jizhi.pojo.NormalQue;

@Mapper
public interface NormalQueDao {

	Integer add(NormalQue normalQue);

	List<NormalQue> queryAll();

}
