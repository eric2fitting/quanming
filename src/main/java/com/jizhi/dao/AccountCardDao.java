package com.jizhi.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jizhi.pojo.AccountCard;

@Mapper
public interface AccountCardDao {
	int save(AccountCard accountCard);

	int del(Integer userId);

	int update(AccountCard accountCard);

	List<AccountCard> queryAll(Integer userId);
}
