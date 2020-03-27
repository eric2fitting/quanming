package com.jizhi.dao;

import com.jizhi.pojo.IdCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface IdCardDao {

	int save(IdCard idCard);

	int update(IdCard idCard);
	//查看身份证认证了几次
	Integer querySizeByIdNum(String idNum);
	@Select("select * from idCard where user_id=#{userId}")
	IdCard queryByUserId(Integer userId);

}
