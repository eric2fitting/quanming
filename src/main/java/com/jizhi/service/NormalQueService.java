package com.jizhi.service;

import com.jizhi.pojo.NormalQue;

import java.util.List;

public interface NormalQueService {

	Integer add(NormalQue normalQue);

	List<NormalQue> queryAll();

	int delete(Integer id);

	int update(NormalQue normalQue);

}
