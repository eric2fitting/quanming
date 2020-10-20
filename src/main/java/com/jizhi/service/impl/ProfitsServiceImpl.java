package com.jizhi.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jizhi.dao.FeedDao;
import com.jizhi.dao.OtherInfoDao;
import com.jizhi.dao.ProfitsDao;
import com.jizhi.dao.UserDao;
import com.jizhi.pojo.Feed;
import com.jizhi.pojo.OtherInfo;
import com.jizhi.pojo.Profits;
import com.jizhi.pojo.User;
import com.jizhi.pojo.vo.FeedExchangeParam;
import com.jizhi.pojo.vo.ShareProfitsVO;
import com.jizhi.pojo.vo.UserInfo;
import com.jizhi.service.ProfitsService;
import com.jizhi.util.RedisService;
@Transactional(rollbackFor = Exception.class)
@Service
public class ProfitsServiceImpl implements ProfitsService{
	
	@Autowired
	private ProfitsDao profitsDao;
	
	@Autowired
	private RedisService redisService;

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private FeedDao feedDao;
	
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
		if(allShareProfit>=shareProfit) {
			Profits profits = new Profits();
			profits.setAnimalProfit(0D);
			profits.setNFC(0D);
			profits.setUserId(null);
			profits.setSharerId(sharerId);
			profits.setShareProfit(-shareProfit);
			return profitsDao.save(profits);
		}
		return 0;
	}
	
	/**
	 * 查询自己现在拥有的所有分享收益
	 * @param id
	 * @return
	 */
	@Override
	public Double queryShareProfit(Integer id) {
		return profitsDao.queryShareProfit(id);
	}
	
	/**
	 * 查询得到的所有分享收益（提现的也不减少）
	 * @param shareId
	 * @return
	 */
	public Double getAllShare(Integer sharerId) {
		return profitsDao.getAllShare(sharerId);
	}

	@Override
	public Double queryAllAnimalProfits(Integer id) {
		
		return profitsDao.queryAllAnimalProfits(id);
	}

	@Override
	public Double queryAllNFC(Integer id) {
		return profitsDao.queryAllNFC(id);
	}
	
	/**
	 *用分享收益兑换饲料页面显示
	 * @param request
	 * @return
	 */
	@Override
	public FeedExchangeParam tryShareProfitsToFeed(Integer userId) {
		Double shareProfit=queryShareProfit(userId);
		//查看用户等级
		User user = userDao.queryById(userId);
		Integer maxExchangeNum=0;
		if(shareProfit!=null) {
			BigDecimal shareProfit_b = new BigDecimal(shareProfit);
			BigDecimal bb = new BigDecimal(10);
			switch (user.getLevel()) {
			case 0:
				maxExchangeNum=shareProfit_b.setScale(0, BigDecimal.ROUND_DOWN).intValue();
				break;
			case 1:
				BigDecimal b1 = new BigDecimal(8);
				maxExchangeNum=shareProfit_b.multiply(bb).divide(b1).setScale(0, BigDecimal.ROUND_DOWN).intValue();
				break;
			case 2:
				BigDecimal b2 = new BigDecimal(7);
				maxExchangeNum=shareProfit_b.multiply(bb).divide(b2).setScale(0, BigDecimal.ROUND_DOWN).intValue();
				break;
			case 3:
				BigDecimal b3 = new BigDecimal(5);
				maxExchangeNum=shareProfit_b.multiply(bb).divide(b3).setScale(0, BigDecimal.ROUND_DOWN).intValue();
				break;

			case 4:
				BigDecimal b4 = new BigDecimal(4);
				maxExchangeNum=shareProfit_b.multiply(bb).divide(b4).setScale(0, BigDecimal.ROUND_DOWN).intValue();
				break;
			}
		}else {
			shareProfit=0D;
		}
		
		FeedExchangeParam response = new FeedExchangeParam();
		response.setLevel(user.getLevel());
		response.setUserId(userId);
		response.setMaxExchangeNum(maxExchangeNum);
		response.setShareProfits(shareProfit);
		return response;
	}
	

	/**
	 *分享收益兑换饲料
	 * @param request
	 * @return
	 */
	@Override
	public Integer shareProfitsToFeed(FeedExchangeParam feedExchangeParam) {
		Integer maxExchangeNum = feedExchangeParam.getMaxExchangeNum();
		Integer exchangeNum = feedExchangeParam.getExchangeNum();
		Double FeedNum=Double.parseDouble(exchangeNum+"");
		if(exchangeNum>maxExchangeNum) {
			return 0;//兑换饲料超过最大值
		}
		Integer userId = feedExchangeParam.getUserId();
		User user = userDao.queryById(userId);
		if(user.getLevel()<2) {
			return 3;//会员等级小于2
		}
		if(!user.getSecondpsw().equals(feedExchangeParam.getSecondPsw())) {
			return 1;//二级密码错误
		}
		//计算需要扣除的分享收益
		BigDecimal b = new BigDecimal(exchangeNum);
		Double shareProfit=0D;
		switch (feedExchangeParam.getLevel()) {
		case 0:
			shareProfit=FeedNum;
			break;
		case 1:
			BigDecimal b1 = new BigDecimal(0.8);
			shareProfit=b.multiply(b1).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			break;
		case 2:
			BigDecimal b2 = new BigDecimal(0.7);
			shareProfit=b.multiply(b2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			break;
		case 3:
			BigDecimal b3 = new BigDecimal(0.5);
			shareProfit=b.multiply(b3).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			break;
		case 4:
			BigDecimal b4 = new BigDecimal(0.4);
			shareProfit=b.multiply(b4).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			break;
		}
		
		//扣除的分享收益
		Profits profits = new Profits();
		profits.setSharerId(userId);
		profits.setShareProfit(-shareProfit);
		profits.setState(2);
		profits.setAnimalProfit(0D);
		profits.setNFC(0D);
		profits.setUserId(null);
		profitsDao.shareProfitsToFeed(profits);
		//增加饲料
		Feed feed = new Feed();
		feed.setUserId(userId);
		feed.setDate(new Date());
		feed.setNum(FeedNum);
		feed.setType(4);
		feedDao.insert(feed);
		return 2;
	}
//	
//	/**
//	 *用NFC币兑换饲料页面显示
//	 * @param request
//	 * @return
//	 */
//	@Override
//	public FeedExchangeParam tryNFCToFeed(Integer userId) {
//		User user = userDao.queryById(userId);
//		Integer NFC = queryAllNFC(userId);
//		FeedExchangeParam feedExchangeParam = new FeedExchangeParam();
//		if(NFC!=null) {
//			feedExchangeParam.setNFC(NFC);
//			feedExchangeParam.setMaxExchangeNum(NFC);
//		}else {
//			feedExchangeParam.setNFC(0);
//			feedExchangeParam.setMaxExchangeNum(0);
//		}
//		feedExchangeParam.setLevel(user.getLevel());
//		feedExchangeParam.setUserId(userId);		
//		return feedExchangeParam;
//	}
//	
//	/**
//	 *NFC币兑换饲料确认页面
//	 * @param request
//	 * @return
//	 */
//	@Override
//	public Integer NFCToFeed(FeedExchangeParam feedExchangeParam) {
//		Integer maxExchangeNum = feedExchangeParam.getMaxExchangeNum();
//		Integer exchangeNum = feedExchangeParam.getExchangeNum(); 
//		Double feedNum=Double.parseDouble(exchangeNum+"");
//		Integer userId = feedExchangeParam.getUserId();
//		User user = userDao.queryById(userId);
//		if(exchangeNum>maxExchangeNum) {
//			return 0;
//		}
//		if(!user.getSecondpsw().equals(feedExchangeParam.getSecondPsw())) {
//			return 1;//二级密码错误
//		}
//		//增加饲料
//		Feed feed = new Feed();
//		feed.setUserId(userId);
//		feed.setNum(feedNum);
//		feed.setType(4);
//		feed.setDate(new Date());
//		feedDao.insert(feed);
//		//扣除NFC币
//		Profits profits = new Profits();
//		profits.setNFC(-exchangeNumD);
//		profits.setUserId(userId);
//		profits.setShareProfit(0D);
//		profits.setAnimalProfit(0D);
//		profits.setSharerId(null);
//		profits.setState(2);
//		profitsDao.shareProfitsToFeed(profits);
//		return 2;
//	}
	/**
	 * 提现列表
	 */
	@Override
	public List<ShareProfitsVO> queryShareProfitsList(String token) {
		Integer sharerId=Integer.parseInt(redisService.get(token));
		List<Profits> list=profitsDao.queryShareProfitsList(sharerId);
		ArrayList<ShareProfitsVO> response = new ArrayList<ShareProfitsVO>();
		if(list.size()>0) {
			for(Profits profits:list) {
				ShareProfitsVO shareProfitsVO = new ShareProfitsVO();
				if(profits.getState()==0) {
					shareProfitsVO.setResult("处理中");
				}else {
					shareProfitsVO.setResult("已处理");
				}
				shareProfitsVO.setDate(profits.getUpdateTime());
				shareProfitsVO.setShareProfit(Math.abs(profits.getShareProfit()));
				response.add(shareProfitsVO);
			}
		}
		return response;
	}


}
