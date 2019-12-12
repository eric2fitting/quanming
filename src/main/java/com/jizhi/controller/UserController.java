package com.jizhi.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jizhi.pojo.FinalResult;
import com.jizhi.pojo.User;
import com.jizhi.pojo.vo.LoginInfo;
import com.jizhi.pojo.vo.MyTeam;
import com.jizhi.pojo.vo.PswInfo;
import com.jizhi.pojo.vo.UserInfo;
import com.jizhi.service.UserSevice;

@RestController
@RequestMapping("qm")
public class UserController {
	@Autowired
	private UserSevice userSevice;
	/*
	 *登陆
	 */
	@RequestMapping("/login")
	public FinalResult login( @RequestBody User user) {
		HashMap<String, Object> map=this.userSevice.query(user);
		FinalResult finalResult = new FinalResult();
		if(map==null) {
			finalResult.setCode("102");
			finalResult.setMsg("账号或密码错误");
		}else {
			finalResult.setCode("100");
			finalResult.setMsg("登陆成功");
			finalResult.setBody(map);
		}
		return finalResult;
	}
	/**
	 * 注册
	 * @param info
	 * @return
	 */
	@RequestMapping("/register")
	public FinalResult register(@RequestBody LoginInfo info) {
		boolean bool=this.userSevice.save(info);
		FinalResult finalResult=new FinalResult();
		if(!bool) {
			finalResult.setCode("103");
			finalResult.setMsg("该手机号已经注册过，或验证码错误");
		}else {
			finalResult.setCode("100");
			finalResult.setMsg("注册成功");
		}
		return finalResult;
		
	}
	
	
	/**
	 * 修改登录密码
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/updatePsw")
	public FinalResult updatePsw(@RequestBody PswInfo pswInfo,HttpServletRequest request) {
		String token=request.getHeader("token");
		int i=this.userSevice.updatePsw(token,pswInfo);
		FinalResult finalResult = new FinalResult();
		if (i>0) { 
			finalResult.setCode("100"); finalResult.setMsg("修改密码成功"); 
		}else {
			finalResult.setCode("104"); finalResult.setMsg("修改密码失败"); 
		}
		return finalResult;
	}
	
	/**
	 * 修改二级密码
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/updateSecondPsw")
	public FinalResult updateSecondPsw(@RequestBody PswInfo pswInfo,HttpServletRequest request) {
		String token=request.getHeader("token");
		int i=this.userSevice.updateSecondPsw(token,pswInfo);
		FinalResult finalResult = new FinalResult();
		
		if (i>0) { 
			finalResult.setCode("100"); finalResult.setMsg("修改密码成功"); 
		}else {
			finalResult.setCode("104"); finalResult.setMsg("修改密码失败"); 
		}
		return finalResult;
	}
	
	
	/**
	 * 发送短信验证码
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/code")
	public FinalResult sendMsgCode(@RequestBody User user) throws Exception {
		String tel=user.getTel();
		boolean  bool=this.userSevice.sendMsgCode(tel);
		FinalResult finalResult=new FinalResult();
		if(bool) {
			finalResult.setCode("100");
			finalResult.setMsg("已发送");
		}else {
			finalResult.setCode("104");
			finalResult.setMsg("发送失败");
		}
		return finalResult;
	}
	
	/**
	 * 忘记密码，短信验证后重置密码
	 * @param info
	 * @return
	 */
	
	@RequestMapping("/renamePsw")
	public FinalResult forgetPsw(@RequestBody LoginInfo info) {
		boolean bool=this.userSevice.forgetPsw(info);
		FinalResult finalResult=new FinalResult();
		if(!bool) {
			finalResult.setCode("103");
			finalResult.setMsg("验证码错误,重新修改");
		}else {
			finalResult.setCode("100");
			finalResult.setMsg("修改成功");
		}
		return finalResult;	
	}
	
	/*
	 *修改用户名
	 */
	@RequestMapping("/updateUserName")
	public FinalResult updateUserName(@RequestBody User user,HttpServletRequest request) {
		String userName=user.getUserName();
		String token = request.getHeader("token");
		Integer i=this.userSevice.updateUserName(userName,token);
		FinalResult finalResult=new FinalResult();
		if(i>0) {
			finalResult.setCode("100");
			finalResult.setMsg("用户名修改成功");
		}else {
			finalResult.setCode("104");
			finalResult.setMsg("用户名修改失败");
		}
		return finalResult;
	}
	
	
	@RequestMapping("/userInfo")
	public FinalResult queryUserInfo(HttpServletRequest request) {
		String token = request.getHeader("token");
		UserInfo  userInfo=userSevice.queryUserInfo(token);
		FinalResult finalResult = new FinalResult();
		if(userInfo==null){
			finalResult.setCode("104");
			finalResult.setMsg("信息有误重新登录");;
		}else {
			finalResult.setCode("100");
			finalResult.setBody(userInfo);
		}
		return finalResult;
	}
	
	
	@RequestMapping("/myTeam")
	public FinalResult queryMyTeam(HttpServletRequest request) {
		String token = request.getHeader("token");
		MyTeam myTeam=userSevice.queryMyTeam(token);
		FinalResult finalResult = new FinalResult();
		finalResult.setCode("100");
		finalResult.setBody(myTeam);
		return finalResult;
	}
	
	@RequestMapping("/share")
	public FinalResult share(HttpServletRequest request) {
		String token = request.getHeader("token");
		String inviteCode=userSevice.share(token);
		FinalResult finalResult = new FinalResult();
		finalResult.setCode("100");
		finalResult.setBody(inviteCode);
		return finalResult;
	}
}
