package com.jizhi.service;

import java.util.HashMap;
import java.util.List;

import com.jizhi.pojo.User;
import com.jizhi.pojo.vo.LoginInfo;
import com.jizhi.pojo.vo.MyTeam;
import com.jizhi.pojo.vo.PswInfo;
import com.jizhi.pojo.vo.UserInfo;

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
	//递归查询用户团队的活跃人数及其他
	//HashMap<String, Object> queryUserLevelFactor(List<User> list,int activeNum,int level2Num,int level3Num);
	void updateLevel(HashMap<String, Object> map);
	int queryActiveMum(User user,int activeMum);
	int queryLevel2Num(User user,int level2Num);
	int queryLevel3Num(User user,int level3Num);
}
