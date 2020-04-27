package com.jizhi.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.jizhi.pojo.Profits;

@Mapper
public interface ProfitsDao {

	Profits selectAllProfits(Integer userId);

	Integer save(Profits profits);
	
	/**
	 * 查询自己为别人带来的总的分享收益
	 * @param id
	 * @return
	 */
	Double queryAllShareProfitToOthers(Profits profits);
	
	/**
	 * 查询现在拥有的总的分享收益
	 * @param id
	 * @return
	 */
	Double queryShareProfit(Integer id);

	Double queryAllAnimalProfits(Integer userId);

	Integer queryAllNFC(Integer userId);
	//添加数据，把分享收益兑换饲料
	void shareProfitsToFeed(Profits profits);

	Double getAllShare(Integer sharerId);
	
	//提现列表
	List<Profits> queryShareProfitsList(Integer sharerId);


}
