package com.jizhi.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jizhi.pojo.ServiceOnline;

@Mapper
public interface ServiceOnlineDao {

	List<ServiceOnline> queryByUserId(Integer userId);

	int addMsg(ServiceOnline serviceOnline);

	int addPic(ServiceOnline serviceOnline);

}
