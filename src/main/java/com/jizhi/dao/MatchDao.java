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
	
	Match queryOnlyByPropertyId(Integer propertyId);

	Match queryById(Integer id);

	void updatePayPic(HashMap<String, Object> hashMap);

	void updateSellerConfirm(Integer matchId);

	List<Match> queryAllByBuyerConfirm();

	List<Match> queryAllBySellerConfirm();

	void deleteById(Integer id);
	
	/**
	 * 将交易驳回，买家的确认变为0
	 * @param id
	 * @return
	 */
	Integer cancelSell(Integer id);
	
	
	void deleteNotPay();

	
	


}
