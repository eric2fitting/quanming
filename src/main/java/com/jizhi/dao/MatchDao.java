package com.jizhi.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jizhi.pojo.Match;

@Mapper
public interface MatchDao {

	Integer add(Match match);

	Match queryByOrderId(Integer id);
	
	Match queryByPropertyId(HashMap<String,Object> map);

	Match queryById(Integer id);

	void updatePayPic(HashMap<String, Object> hashMap);

	void updateSellerConfirm(Integer matchId);

	List<Match> queryAllByBuyerConfirm();

	List<Match> queryAllBySellerConfirm();


}
