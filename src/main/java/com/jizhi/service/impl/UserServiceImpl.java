package com.jizhi.service.impl;
                
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jizhi.dao.UserDao;
import com.jizhi.pojo.Profits;
import com.jizhi.pojo.User;
import com.jizhi.pojo.vo.LoginInfo;
import com.jizhi.pojo.vo.MyTeam;
import com.jizhi.pojo.vo.PswInfo;
import com.jizhi.pojo.vo.TeamMate;
import com.jizhi.pojo.vo.UserInfo;
import com.jizhi.service.ProfitsService;
import com.jizhi.service.PropertyService;
import com.jizhi.service.UserSevice;
import com.jizhi.util.RedisService;
import com.jizhi.util.SMS;
import com.sun.glass.ui.Size;

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
	 * 忘记密码重新设置
	 */
	@Override
	public boolean forgetPsw(LoginInfo info) {
		String tel = info.getTel();
		String msgCode = info.getCode();
		String password = info.getPassword();
		//从redis中查询该手机对应的验证码
		String record = this.redisService.get(tel);
		HashMap<String, Object> map=new HashMap<String, Object>();
		map.put("tel", tel);
		map.put("password", password);
		//验证码一致，更新密码
		if(msgCode.equals(record)) {
			this.userDao.updatePswByTel(map);
			return true;
		}
		else { 
			return false; 
			}

	}

	
	
	
	
	/*
	 * 生成随机数
	 */
	public String random() {
		Random random = new Random();
		return random.nextInt(899999)+100000+"";
	}
	
	
	/**
	 * 修改用户名
	 */

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
	
	
	/**
	 * 查询用户个人信息资产等
	 */
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
		if(profits!=null) {
			userInfo.setAnimalProfit(profits.getAnimalProfit());
			userInfo.setShareProfit(profits.getShareProfit());
			userInfo.setNFC(profits.getNFC());
		}
		return userInfo;
	}

	/**
	 * 修改密码
	 */
	@Override
	public int updatePsw(String token, PswInfo pswInfo) {
		//根据token从redis中取出UserId
		String string = this.redisService.get(token);
		Integer userId=Integer.parseInt(string);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("oldPsw", pswInfo.getOldPsw());
		map.put("newPsw", pswInfo.getNewPsw());
		return userDao.updatePsw(map);
	}

	/**
	 * 修改二级密码
	 */
	@Override
	public int updateSecondPsw(String token, PswInfo pswInfo) {
		//根据token从redis中取出UserId
		String string = this.redisService.get(token);
		Integer userId=Integer.parseInt(string);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("newPsw", pswInfo.getNewPsw());
		return userDao.updateSecondPsw(map);
	}

	/**
	 * 查找用户的团队信息
	 */
	@Override
	public MyTeam queryMyTeam(String token) {
		String string = redisService.get(token);
		Integer id = Integer.parseInt(string);
		User record = userDao.queryById(id);
		//用户自己的邀请码也就是别人被邀请的码
		String invitedCode = record.getInviteCode();
		List<User> users=userDao.queryByInvitedCode(invitedCode);
		ArrayList<TeamMate> teamMates = new ArrayList<TeamMate>();
		Double teamProfit=0.00;
		Integer activedNum=0;
		Integer activeNum=0;
		Integer unActiveNum=0;
		int numbers=users.size();
		for(User user:users) {
			TeamMate mate = new TeamMate();
			mate.setName(user.getUserName());//设置团员名字
			String state = user.getState();
			mate.setState(state);//设置团员活跃状态
			mate.setID("ID:"+user.getTel());//设置团员ID
			Integer userId=user.getId();
			Double totalMoney = propertyService.queryTotalMonet(userId);
			//查找该团员所有交易得到分享
			
			teamProfit=teamProfit+totalMoney;
			if(state=="已激活") {
				activeNum++;
			}else if (state=="活跃") {
				activedNum++;
			}else if (state=="未激活用户") {
				unActiveNum++;
			}
		}
		MyTeam myTeam = new MyTeam();
		myTeam.setActivedNum(activedNum);
		myTeam.setActiveNum(activeNum);
		myTeam.setTeamNum(teamMates.size());
		myTeam.setUnActiveNum(unActiveNum);
		myTeam.setTeamMates(teamMates);
		myTeam.setTeamProfit(teamProfit);
		return myTeam;
	}


	
}
