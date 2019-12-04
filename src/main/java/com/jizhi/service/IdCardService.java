package com.jizhi.service;

import javax.servlet.http.HttpServletRequest;

import com.jizhi.pojo.IdCard;

public interface IdCardService {

	int saveIdCard(IdCard idCard,HttpServletRequest request,String token);

	int updateIdCard(IdCard idCard,HttpServletRequest request,String token);

}
