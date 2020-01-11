package com.jizhi.service;

import com.jizhi.pojo.IdCard;

public interface IdCardService {

	int saveIdCard(IdCard idCard,String token);

	int updateIdCard(IdCard idCard,String token);

	Integer queryIsConfirmed(String token);

}
