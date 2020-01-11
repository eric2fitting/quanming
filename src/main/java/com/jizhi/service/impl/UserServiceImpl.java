package com.jizhi.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional(rollbackFor = Exception.class)
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
				redisService.set(key, value,60*60*24*7);
				map.put("token", key);
				map.put("user", record);
				//更新用户的cid
				userDao.updateCid(user);
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
		System.out.println(randomNum);
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
//				String random;
//				while(true) {
//					random = random();
//					User u=this.userDao.queryByRandom(random);
//					if(u==null) {
//						break;
//					}
//				}
//				user.setInviteCode(random);
				//用自己的手机号生成验证码
				user.setInviteCode(tel);
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
//
//	@Override
//	public Integer updateUserName(String userName, String token) {
//		String user_id = this.redisService.get(token);
//		int userId = Integer.parseInt(user_id);
//		HashMap<String, Object> map = new HashMap<String, Object>();
//		map.put("userId", userId);
//		map.put("userName", userName);
//		Integer i=userDao.updateUserName(map);
//		return i;
//	}
	
	
	/**
	 * 查询用户个人信息资产等
	 */
	@Override
	public UserInfo queryUserInfo(String token) {
		String user_id = this.redisService.get(token);
		Integer id = Integer.parseInt(user_id);
		//根据userId查询user的tel和userName
		User user=userDao.queryById(id);
		//根据userId查询总资产
		Double totalMoney=propertyService.queryTotalMonet(id);
		//查询总的动物收益
		Double allAnimalProfits=profitsService.queryAllAnimalProfits(id);
		//查询中的NFC币
		Integer nfcs=profitsService.queryAllNFC(id);
		UserInfo userInfo = new UserInfo();
		userInfo.setUserName(user.getUserName());
		userInfo.setTel(user.getTel());
		if(totalMoney==null) {
			userInfo.setTotalMoney(0D);
		}else {
			userInfo.setTotalMoney(totalMoney);
		}
		if(allAnimalProfits!=null) {
			userInfo.setAnimalProfit(allAnimalProfits);
		}else {
			userInfo.setAnimalProfit(0D);
		}
		if (nfcs!=null) {
			userInfo.setNFC(nfcs);
		}else {
			userInfo.setNFC(0);
		}
		Double shareProfit=profitsService.queryShareProfit(id);
		if(shareProfit==null) {
			userInfo.setShareProfit(0D);
		}else {
			userInfo.setShareProfit(shareProfit);
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
		MyTeam result=new MyTeam();
		//用户自己的邀请码也就是别人被邀请的码
		String invitedCode = record.getInviteCode();
		if(StringUtils.isNotEmpty(invitedCode)) {
			List<User> users=userDao.queryByInvitedCode(invitedCode);
			ArrayList<TeamMate> teamMates = new ArrayList<TeamMate>();
			Double allShareProfit = profitsService.queryShareProfit(id);
			Integer level_1_num=0; 
			Integer level_2_num=0; 
			Integer level_3_num=0; 
			if(allShareProfit==null) {
				result.setTeamProfit(0D);
			}else {
				result.setTeamProfit(allShareProfit);//总得分享收益
			}
			if(users.size()>0) {
				for(User user:users) {
					level_1_num++;
					TeamMate mate = new TeamMate();
					mate.setName(user.getUserName());//设置团员名字
					mate.setLevel(1);//直推用户
					mate.setState(user.getState());//设置团员活跃状态
					//查看自己给用户带来了多少分享收益
					Profits p=new Profits();
					p.setUserId(user.getId());
					p.setSharerId(id);
					Double shareProfits_1=profitsService.queryAllShareProfitToOthers(p);
					if(shareProfits_1==null) {
						mate.setShareMoney(0D);
					}else {
						mate.setShareMoney(shareProfits_1);
					}
					teamMates.add(mate);//放入集合中
					//查看是否有二代会员
					String invitedCode_2= user.getInviteCode();
					List<User> users_2=userDao.queryByInvitedCode(invitedCode_2);//二代会员
					//遍历二代会员
					if(users_2.size()>0) {
						for(User user_2:users_2) {
							level_2_num++;
							TeamMate mate_2 = new TeamMate();
							mate_2.setLevel(2);
							mate_2.setName(user_2.getUserName());
							mate_2.setState(user_2.getState());
							//查看自己给用户带来了多少分享收益
							Profits p2=new Profits();
							p2.setUserId(user_2.getId());
							p2.setSharerId(id);
							Double shareProfits_2=profitsService.queryAllShareProfitToOthers(p2);
							if(shareProfits_2==null) {
								mate_2.setShareMoney(0D);
							}else {
								mate_2.setShareMoney(shareProfits_2);
							}
							teamMates.add(mate_2);//放入集合中
							//查看是否有二代会员
							String invitedCode_3= user_2.getInviteCode();
							List<User> users_3=userDao.queryByInvitedCode(invitedCode_3);//二代会员
							//遍历三代会员
							if(users_2.size()>0) {
								for(User user_3:users_3) {
									level_3_num++;
									TeamMate mate_3 = new TeamMate();
									mate_3.setLevel(3);
									mate_3.setName(user_3.getUserName());
									mate_3.setState(user_3.getState());
									//查看自己给用户带来了多少分享收益
									Profits p3=new Profits();
									p3.setUserId(user_3.getId());
									p3.setSharerId(id);
									Double shareProfits_3=profitsService.queryAllShareProfitToOthers(p3);
									if(shareProfits_3==null) {
										mate_3.setShareMoney(0D);
									}else {
										mate_3.setShareMoney(shareProfits_3);
									}
									teamMates.add(mate_3);//放入集合中
								}
							}
						}
					}
				}
				result.setLevel_1_num(level_1_num);
				result.setLevel_2_num(level_2_num);
				result.setLevel_3_num(level_3_num);
				result.setTeamMates(teamMates);
				result.setTeamNum(level_1_num+level_2_num+level_3_num);
			}else {
				result.setLevel_1_num(0);
				result.setLevel_2_num(0);
				result.setLevel_3_num(0);
				result.setTeamMates(null);
				result.setTeamNum(0);
				result.setTeamProfit(0D);
			}
		}else {
			result=null;
			
		}
		return result;
	}

	/**
	 * 分享，返回邀请码
	 */
	@Override
	public String share(String token) {
		int id = Integer.parseInt(redisService.get(token));
		User user = userDao.queryById(id);
		return user.getInviteCode();
	}
	
	/**
	 *更改冻结状态
	 */
	@Override
	public void updateIsFrozen(HashMap<String, Object> map) {
		userDao.updateIsFrozen(map);
	}

	
	/**
	 * 查找管理员
	 */
	@Override
	public List<User> queryAdmin() {
		return userDao.queryAdmin();
	}

	
	/**
	 * 退出，删除token
	 */
	@Override
	public Integer quit(String token) {
		try {
			redisService.delete(token);
			return 1;
		} catch (Exception e) {
		}
		
		return 0;
	}


	@Override
	public void updateIsConfirmed(Integer id) {
		userDao.updateIsConfirmed(id);
		
	}
	
	
}
