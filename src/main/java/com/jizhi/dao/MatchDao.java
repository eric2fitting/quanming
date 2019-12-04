package com.jizhi.dao;

import org.apache.ibatis.annotations.Mapper;

import com.jizhi.pojo.Match;

@Mapper
public interface MatchDao {

	Integer add(Match match);

	Match queryByOrderId(Integer id);

}
