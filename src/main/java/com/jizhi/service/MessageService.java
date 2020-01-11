package com.jizhi.service;

import com.jizhi.pojo.Message;

import java.util.List;

public interface MessageService {

	List<Message> queryAll();

	List<Message> queryByType(Integer type);

	Integer save(Message message);

	Message queryLatestMsg();

	Integer delete(Integer id);


}
