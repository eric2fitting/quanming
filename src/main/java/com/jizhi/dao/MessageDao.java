package com.jizhi.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jizhi.pojo.Message;

@Mapper
public interface MessageDao {
	Integer add(Message message);
	List<Message> queryAll();
	List<Message> queryByType(Integer type);
	Message queryLatestMsg();
}
