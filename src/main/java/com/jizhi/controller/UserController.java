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
	public FinalResult login( User user) {
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
	 * 修改密码
	 * code=0，表示修改密码；如果是1，表示2修改二级密码。
	 * @param request
	 * @return
	 */
	@RequestMapping("/updatePsw")
	public FinalResult updatePsw(HttpServletRequest request) {
		String token=request.getParameter("token");
		String code = request.getParameter("code");
		String oldPsw = request.getParameter("oldPsw");
		String newPsw = request.getParameter("newPsw");
		int i=this.userSevice.updatePsw(code,token,oldPsw,newPsw);;	
		FinalResult finalResult = new FinalResult();
		if (i>0) {
			finalResult.setCode("100");
			finalResult.setMsg("修改密码成功");
		}else {
			finalResult.setCode("104");
			finalResult.setMsg("修改密码失败");
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
	
	@RequestMapping("/updateUserName")
	public FinalResult updateUserName(HttpServletRequest request) {
		String userName=request.getParameter("userName");
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
	
	
	@RequestMapping("/query")
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
	
}
