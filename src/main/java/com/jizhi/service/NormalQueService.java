package com.jizhi.service;

import java.util.List;

import com.jizhi.pojo.NormalQue;

public interface NormalQueService {

	Integer add(NormalQue normalQue);

	List<NormalQue> queryAll();

}
