package com.jizhi.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jizhi.dao.FeedDao;
import com.jizhi.dao.UserDao;
import com.jizhi.pojo.Feed;
import com.jizhi.pojo.User;
import com.jizhi.pojo.vo.FeedSendParam;
import com.jizhi.service.FeedService;
@Transactional(rollbackFor = Exception.class)
@Service
public class FeedServiceImpl implements FeedService{
	
	@Autowired
	private FeedDao feedDao;
	
	@Autowired
	private UserDao userDao;
	/**
	 * 查询用户当前所有饲料
	 */
	@Override
	public Double queryTotalFeed(Integer userId) {
		Double totalFeed=feedDao.queryTotalFeedByUserId(userId);
		if(totalFeed==null) {
			totalFeed=0D;
		}else {
			totalFeed=new BigDecimal(totalFeed).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
		}
		return totalFeed;
	}
	
	/**
	 * 查询用户饲料明细
	 */
	@Override
	public List<Feed> queryFeedDetail(Integer userId) {
		return feedDao.queryFeedDetail(userId);
	}

	/**
	 * 转赠饲料
	 */
	@Override
	public String sendFeed(FeedSendParam feedSendParam) {
		Double sendNum = feedSendParam.getNum();//转赠数量
		Integer userId = feedSendParam.getUserId();//用户id
		String tel = feedSendParam.getTel();//受赠者手机
		if(sendNum<=100D) {
			return "转赠数量必须大于100";
		}
		Double total = feedDao.queryTotalFeedByUserId(userId);
		if(total<sendNum) {
			return "你所拥有的饲料不足";
		}
		User record = userDao.queryByTel(tel);
		if(record==null) {
			return "你所转赠的用户不存在";
		}
		User user = userDao.queryById(userId);
		if(!feedSendParam.getSeconPsw().equals(user.getSecondpsw())) {
			return "二级密码错误";
		}
		if(user.getTel().equals(tel)) {
			return "不能转让给自己";
		}
		//减少用户饲料
		Feed feed = new Feed();
		feed.setDate(new Date());
		feed.setNum(-sendNum);
		feed.setType(5);
		feed.setUserId(userId);
		feedDao.insert(feed);
		//给受赠者添加
		feed.setType(3);
		feed.setNum(sendNum);
		feed.setUserId(record.getId());
		feedDao.insert(feed);
		return null;
	}
	
}
