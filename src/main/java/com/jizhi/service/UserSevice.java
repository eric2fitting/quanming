package com.jizhi.service;

import java.util.HashMap;

import com.jizhi.pojo.User;
import com.jizhi.pojo.vo.LoginInfo;
import com.jizhi.pojo.vo.UserInfo;

public interface UserSevice {

	HashMap<String, Object> query(User user);
	boolean sendMsgCode(String tel) throws Exception;
	boolean save(LoginInfo info);
	int updatePsw(String code, String token, String oldPsw, String newPsw);
	boolean forgetPsw(LoginInfo info);
	Integer updateUserName(String userName, String token);
	UserInfo queryUserInfo(String token);
}
