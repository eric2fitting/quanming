package com.jizhi.service.impl;

import com.jizhi.dao.OtherInfoDao;
import com.jizhi.dao.ProfitsDao;
import com.jizhi.pojo.OtherInfo;
import com.jizhi.pojo.Profits;
import com.jizhi.pojo.vo.UserInfo;
import com.jizhi.service.ProfitsService;
import com.jizhi.util.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Transactional(rollbackFor = Exception.class)
@Service
public class ProfitsServiceImpl implements ProfitsService{
	
	@Autowired
	private ProfitsDao profitsDao;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private OtherInfoDao otherInfoDao;
	
	/**
	 * 根据用户id查询他各部分的总收益
	 */
	@Override
	public Profits selectAllProfits(Integer id) {
		return profitsDao.selectAllProfits(id);
	}

	@Override
	public void add(Profits profits) {
		profitsDao.save(profits);
		
	}
	
	/**
	 * 查询自己为别人带来的总的分享收益
	 * @param id
	 * @return
	 */
	@Override
	public Double queryAllShareProfitToOthers(Profits profits) {
		return profitsDao.queryAllShareProfitToOthers(profits);
	}
	
	/**
	 * 提现
	 */
	@Override
	public Integer updateShareProfit(String token, UserInfo userInfo) {
		Integer sharerId=Integer.parseInt(redisService.get(token));
		Double shareProfit = userInfo.getShareProfit();
		//查询自己的总分享收益
		Double allShareProfit = profitsDao.queryShareProfit(sharerId);
		//查询提现金额
		OtherInfo otherInfo = otherInfoDao.query();
		Double limit = otherInfo.getEarnMoney();
		if((shareProfit>=limit) && (allShareProfit>=shareProfit)) {
			Profits profits = new Profits();
			profits.setSharerId(sharerId);
			profits.setShareProfit(-shareProfit);
			return profitsDao.save(profits);
		}
		return 0;
	}
	
	/**
	 * 查询自己得到的总得分享收益
	 * @param id
	 * @return
	 */
	@Override
	public Double queryShareProfit(Integer id) {
		
		return profitsDao.queryShareProfit(id);
	}

	@Override
	public Double queryAllAnimalProfits(Integer id) {
		
		return profitsDao.queryAllAnimalProfits(id);
	}

	@Override
	public Integer queryAllNFC(Integer id) {
		return profitsDao.queryAllNFC(id);
	}


}
