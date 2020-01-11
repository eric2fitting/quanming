package com.jizhi.dao;

import com.jizhi.pojo.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageDao {
	Integer add(Message message);
	List<Message> queryAll();
	List<Message> queryByType(Integer type);
	Message queryLatestMsg();
	Integer delete(Integer id);
}
