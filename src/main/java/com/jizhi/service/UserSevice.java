package com.jizhi.service;

import com.jizhi.pojo.User;
import com.jizhi.pojo.vo.LoginInfo;
import com.jizhi.pojo.vo.MyTeam;
import com.jizhi.pojo.vo.PswInfo;
import com.jizhi.pojo.vo.UserInfo;

import java.util.HashMap;
import java.util.List;

public interface UserSevice {

	HashMap<String, Object> query(User user);
	boolean sendMsgCode(String tel) throws Exception;
	boolean save(LoginInfo info);
	boolean forgetPsw(LoginInfo info);
	//Integer updateUserName(String userName, String token);
	UserInfo queryUserInfo(String token);
	int updatePsw(String token, PswInfo pswInfo);
	int updateSecondPsw(String token, PswInfo pswInfo);
	MyTeam queryMyTeam(String token);
	String share(String token);
	void updateIsFrozen(HashMap<String, Object> map);
	List<User> queryAdmin();
	Integer quit(String token);
	void updateIsConfirmed(Integer userId);
}
