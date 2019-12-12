package com.jizhi.service;

import java.util.List;

import com.jizhi.pojo.ServiceOnline;

public interface ServiceOnlineService {

	List<ServiceOnline> queryByUserId(Integer userId);

	int addMsg(ServiceOnline serviceOnline);

	int addPic(ServiceOnline serviceOnline);

}
