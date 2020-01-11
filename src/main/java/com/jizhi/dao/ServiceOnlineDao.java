package com.jizhi.dao;

import com.jizhi.pojo.ServiceOnline;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ServiceOnlineDao {

	List<ServiceOnline> queryByUserId(Integer userId);

	int addMsg(ServiceOnline serviceOnline);

	int addPic(ServiceOnline serviceOnline);

	List<ServiceOnline> queryNotAnswered();

	int addAnswer(ServiceOnline serviceOnline);

	int updateIsAnswered(Integer userId);

}
