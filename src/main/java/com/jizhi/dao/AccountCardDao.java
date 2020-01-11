package com.jizhi.dao;

import com.jizhi.pojo.AccountCard;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountCardDao {
	int save(AccountCard accountCard);

	int del(Integer id);

	int update(AccountCard accountCard);

	List<AccountCard> queryAll(Integer userId);
}
