package com.jizhi.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jizhi.pojo.Feed;

@Mapper
public interface FeedDao {
	
	//查询总的饲料
	Double queryTotalFeedByUserId(Integer userId);
	//查询饲料明细
	List<Feed> queryFeedDetail(Integer userId);
	//添加数据
	void insert(Feed feed);

}
