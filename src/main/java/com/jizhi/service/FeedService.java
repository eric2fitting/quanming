package com.jizhi.service;

import java.util.List;

import com.jizhi.pojo.Feed;
import com.jizhi.pojo.vo.FeedSendParam;

public interface FeedService {

	Double queryTotalFeed(Integer userId);
	
	List<Feed> queryFeedDetail(Integer userId);

	String sendFeed(FeedSendParam feedSendParam);

}
