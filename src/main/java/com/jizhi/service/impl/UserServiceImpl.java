package com.jizhi.service.impl;
                
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jizhi.dao.UserDao;
import com.jizhi.pojo.Profits;
import com.jizhi.pojo.User;
import com.jizhi.pojo.vo.LoginInfo;
import com.jizhi.pojo.vo.UserInfo;
import com.jizhi.service.ProfitsService;
import com.jizhi.service.PropertyService;
import com.jizhi.service.UserSevice;
import com.jizhi.util.RedisService;
import com.jizhi.util.SMS;

@Service
public class UserServiceImpl implements UserSevice{
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private PropertyService propertyService;
	
	@Autowired
	private ProfitsService profitsService;
	
	/**
	 * 登录验证
	 */
	@Override
	public HashMap<String, Object> query(User user) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String key;
		String value;
		User record=this.userDao.queryByTel(user.getTel());
		//用户不存在
		if(record==null) {
			return null;
		}else {
			//验证密码是否正确
			if(record.getPassword().equals(user.getPassword())) {
				//登陆成功生成token存在redis中并返回给前端
				key=UUID.randomUUID().toString();
				value=record.getId()+"";
				redisService.set(key, value,60*60*24*30);
				map.put("token", key);
				map.put("user", record);
				return map;
			}else {
				//密码错误
				return null;
			}
		}
	}
	
	
	/**
	 * 给目标手机发送验证码
	 * @throws Exception 
	 */
	public boolean sendMsgCode(String tel) throws Exception {
		
		String randomNum = random();
		Boolean bool = SMS.sendmsg(tel, randomNum);
		//将手机号验证码保存在redis以便验证
		this.redisService.set(tel, randomNum,60*3);
		return bool;
	}
	
	/**
	 * 注册
	 * 1、验证验证码是否正确
	 * 2、保存用户
	 */
	@Override
	public boolean save(LoginInfo info) {
		String tel = info.getTel();
		//判断该电话是否已经注册，没有才能注册
		User hasUser = userDao.queryByTel(tel);
		if(hasUser!=null) {
			return false;
		}else {
			String code = info.getCode();
			//从redis中查询该手机对应的验证码
			String record = this.redisService.get(tel);
			//验证码一致，保存用户
			if(code.equals(record)) {
				User user = new User();
				user.setTel(tel);
				user.setPassword(info.getPassword());
				user.setSecondpsw(info.getSecondPassword());
				//TODO 邀请码是否为空还没判断
				user.setInvitedCode(info.getInvitedCode());
				//随机生成6位数自己的邀请码，判断是否重复，没有则保存
				String random;
				while(true) {
					random = random();
					User u=this.userDao.queryByRandom(random);
					if(u==null) {
						break;
					}
				}
				user.setInviteCode(random);
				this.userDao.save(user);
				return true;
			}
			//验证码不一致，
			else {
				return false;
			}
		}
		
	}
	/**
	 * 修改密码
	 */
	@Override
	public int updatePsw(String code, String token, String oldPsw, String newPsw) {
		int i;
		HashMap<String, Object> map = new HashMap<String, Object>();
		//表示修改密码
		if("0".equals(code)) {
			map.put("oldPsw", oldPsw);
			map.put("newPsw", newPsw);
			i=this.userDao.updatePsw(map);
			//修改二级密码
		}else if ("1".equals(code)) {
			String user_Id = this.redisService.get(token);
			int userId = Integer.parseInt(user_Id);
			map.put("userId", userId);
			map.put("newPsw", newPsw);
			i=this.userDao.updateSecondPsw(map);
		}else {
			i=0;
		}
		return i;
	}
	/**
	 * 忘记密码重新设置
	 */
	
	
	@Override
	public boolean forgetPsw(LoginInfo info) {
		String tel = info.getTel();
		//String msgCode = info.getCode();
		String password = info.getPassword();
		//从redis中查询该手机对应的验证码
		//String record = this.redisService.get(tel);
		HashMap<String, Object> map=new HashMap<String, Object>();
		map.put("tel", tel);
		map.put("password", password);
		//验证码一致，更新密码
		//if(msgCode.equals(record)) {
			this.userDao.updatePswByTel(map);
			
			return true;
		//}
		//验证码不一致，
		/*
		 * else { return false; }
		 */
	}

	
	
	
	
	/*
	 * 生成随机数
	 */
	public String random() {
		Random random = new Random();
		return random.nextInt(899999)+100000+"";
	}


	@Override
	public Integer updateUserName(String userName, String token) {
		String user_id = this.redisService.get(token);
		int userId = Integer.parseInt(user_id);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("userName", userName);
		Integer i=userDao.updateUserName(map);
		return i;
	}


	@Override
	public UserInfo queryUserInfo(String token) {
		String user_id = this.redisService.get(token);
		int id = Integer.parseInt(user_id);
		//根据userId查询user的tel和userName
		User user=userDao.queryById(id);
		//根据userId查询总资产
		Double totalMoney=propertyService.queryTotalMonet(id);
		//根据userId查询总的收益、分享收益、NFC币
		Profits profits=profitsService.selectAllProfits(id);
		UserInfo userInfo = new UserInfo();
		userInfo.setUserName(user.getUserName());
		userInfo.setTel(user.getTel());
		userInfo.setTotalMoney(totalMoney);
		userInfo.setAnimalProfit(profits.getAnimalProfit());
		userInfo.setShareProfit(profits.getShareProfit());
		userInfo.setNFC(profits.getNFC());
		return userInfo;
	}


	
}
