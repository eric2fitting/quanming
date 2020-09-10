package com.jizhi.service;

import java.util.List;

import com.jizhi.pojo.vo.FeedSendParam;
import com.jizhi.pojo.vo.FeedVO;

public interface FeedService {

	Double queryTotalFeed(Integer userId);
	
	List<FeedVO> queryFeedDetail(Integer userId);

	String sendFeed(FeedSendParam feedSendParam);

}
