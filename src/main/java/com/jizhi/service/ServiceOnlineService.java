package com.jizhi.service;

import com.jizhi.pojo.ServiceOnline;
import com.jizhi.pojo.User;

import java.util.List;

public interface ServiceOnlineService {

	List<ServiceOnline> queryByUserId(Integer userId);

	int addMsg(ServiceOnline serviceOnline);

	int addPic(ServiceOnline serviceOnline);

	List<User> queryNotAnswered();

	int addAnswer(ServiceOnline serviceOnline);

}
