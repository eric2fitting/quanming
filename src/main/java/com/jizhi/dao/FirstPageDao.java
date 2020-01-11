package com.jizhi.dao;

import com.jizhi.pojo.FirstPage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FirstPageDao {

	FirstPage query();

}
