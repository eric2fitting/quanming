package com.jizhi.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ExchangeRateDao {
	
	@Select("select rate from exchange_rate where id=1")
	public Double getRate();
}
