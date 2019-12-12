package com.jizhi.service;

import java.util.List;

import com.jizhi.pojo.Message;

public interface MessageService {

	List<Message> queryAll();

	List<Message> queryByType(Integer type);

	Integer save(Message message);

	Message queryLatestMsg();


}
