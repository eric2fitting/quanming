package com.jizhi.dao;

import com.jizhi.pojo.IdCard;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IdCardDao {

	int save(IdCard idCard);

	int update(IdCard idCard);

}
