package com.jizhi.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jizhi.pojo.User;

@Mapper
public interface UserDao {
	//根据电话查找用户
	User queryByTel(String tel);
	//保存用户
	void save(@Param("user") User user);
	//根据邀请码查找用户
	User queryByRandom(String inviteCode);
	//根据旧密码修改新密码
	int updatePsw(HashMap<String, Object> map);
	//修改userId二级密码
	int updateSecondPsw(HashMap<String, Object> map);
	
	//忘记密码后根据手机号修改密码
	void updatePswByTel(HashMap<String, Object> map);
	
	//根据id查询user
	User queryById(int id);
	//根据id修改用户名
	Integer updateUserName(HashMap<String, Object> map);
	//根据被邀请的邀请码查找用户列表
	List<User> queryByInvitedCode(String invitedCode);

	

}
