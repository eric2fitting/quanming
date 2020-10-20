package com.jizhi.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jizhi.dao.FeedDao;
import com.jizhi.dao.UserDao;
import com.jizhi.pojo.Feed;
import com.jizhi.pojo.User;
import com.jizhi.pojo.vo.FeedSendParam;
import com.jizhi.pojo.vo.FeedVO;
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
	
	private static final Logger log = LoggerFactory.getLogger(FeedServiceImpl.class);
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
	public List<FeedVO> queryFeedDetail(Integer userId) {
		ArrayList<FeedVO> result = new ArrayList<FeedVO>();
		List<Feed> feedList = feedDao.queryFeedDetail(userId);
		for(Feed feed:feedList) {
			FeedVO feedVO = new FeedVO();
			feedVO.setId(feed.getId());
			feedVO.setDate(feed.getDate());
			feedVO.setNum(feed.getNum());
			feedVO.setType(feed.getType());
			feedVO.setUserId(feed.getUserId());
			if(feed.getType()==3 || feed.getType()==5) {
				User other = userDao.queryById(feed.getOtherId());
				StringBuilder sb = new StringBuilder(other.getTel());
				sb=sb.replace(3, 7, "****");
				feedVO.setPhone(sb.toString());
			}
			result.add(feedVO);
		}
		return result;
	}

	/**
	 * 转赠饲料
	 */
	@Override
	public String sendFeed(FeedSendParam feedSendParam) {
		Double sendNum = feedSendParam.getNum();//转赠数量
		Integer userId = feedSendParam.getUserId();//用户id
		String tel = feedSendParam.getTel();//受赠者手机
		if(sendNum<10D || sendNum%10!=0) {
			return "转赠数量必须是10的倍数";
		}
		Double total = feedDao.queryTotalFeedByUserId(userId);
		if(total<sendNum+20) {
			return "转赠后剩余金券不能小于20";
		}
		User record = userDao.queryByTel(tel);//获赠者
		User user = userDao.queryById(userId);//赠送者
		if(user.getLevel()==0) {
			return "2星会员才能进行转赠";
		}
		if(record==null) {
			return "你所转赠的用户不存在";
		}else {
			//判断获赠者是否是自己的下线
			log.info("赠送者id"+user.getId());
			if(checkRelation(user, record, 0)==0){
				return "只能转赠给团队里的成员";
			}
		}
		if(!feedSendParam.getSeconPsw().equals(user.getSecondpsw())) {
			return "二级密码错误";
		}
		//减少用户饲料
		Feed feed = new Feed();
		feed.setDate(new Date());
		feed.setNum(-sendNum);
		feed.setType(5);
		feed.setUserId(userId);
		feed.setOtherId(record.getId());//获赠者id
		feedDao.insert(feed);
		//给受赠者添加
		feed.setType(3);
		feed.setNum(sendNum);
		feed.setUserId(record.getId());
		feed.setOtherId(userId);//赠送者id
		feedDao.insert(feed);
		return null;
	}
	
	/**
	 * 判断获赠者是不是转赠者团队里的成员
	 * @return
	 */
	public int checkRelation(User sender,User recipient,int num) {
		String invitedCode = recipient.getInvitedCode();
		if(StringUtils.isNotEmpty(invitedCode)) {
			User inviter = userDao.queryByInviteCode(invitedCode);
			if(inviter==null) {
				return num;
			}
			log.info("接收者上级id"+inviter.getId());
			if(inviter.getId().equals(sender.getId())) {
				return 1;
			}
			num=checkRelation(sender,inviter,num);
		}		
		return num;
	}
	
}
