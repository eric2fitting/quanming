package com.jizhi.dao;

import com.jizhi.pojo.NormalQue;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NormalQueDao {

	Integer add(NormalQue normalQue);

	List<NormalQue> queryAll();

	int delete(Integer id);

	int update(NormalQue normalQue);
	

}
