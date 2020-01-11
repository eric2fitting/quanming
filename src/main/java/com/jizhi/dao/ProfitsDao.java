package com.jizhi.dao;

import com.jizhi.pojo.Profits;
import org.apache.ibatis.annotations.Mapper;

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
	 * 查询自己得到的总得分享收益
	 * @param id
	 * @return
	 */
	Double queryShareProfit(Integer id);

	Double queryAllAnimalProfits(Integer userId);

	Integer queryAllNFC(Integer userId);


}
