package com.jizhi.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jizhi.pojo.FinalResult;
import com.jizhi.pojo.ServiceOnline;
import com.jizhi.service.ServiceOnlineService;
import com.jizhi.util.RedisService;

@RestController
@RequestMapping("serviceOnline")
public class ServiceOnlineController {
	@Autowired
	private ServiceOnlineService serviceOnlineService;
	
	@Autowired
	private RedisService redisService;
	
	@RequestMapping("/query")
	public FinalResult query(HttpServletRequest request) {
		String token = request.getHeader("token");
		Integer userId = Integer.parseInt(redisService.get(token));
		List<ServiceOnline> list=serviceOnlineService.queryByUserId(userId);
		FinalResult finalResult = new FinalResult();
		finalResult.setCode("100");
		finalResult.setBody(list);
		return finalResult;
	}
	
	@RequestMapping("/sendMsg")
	public FinalResult addMsg(HttpServletRequest request,@RequestBody ServiceOnline serviceOnline) {
		String token = request.getHeader("token");
		Integer userId = Integer.parseInt(redisService.get(token));
		serviceOnline.setUserId(userId);
		int i=serviceOnlineService.addMsg(serviceOnline);
		FinalResult finalResult = new FinalResult();
		if(i>0) {
			finalResult.setCode("100");
			finalResult.setMsg("发送成功");
		}else{
			finalResult.setCode("104");
			finalResult.setMsg("发送失败");
		}
		
		return finalResult;
	}
	
	@RequestMapping("/sendPic")
	public FinalResult addPic(HttpServletRequest request,@RequestBody ServiceOnline serviceOnline) {
		String token = request.getHeader("token");
		Integer userId = Integer.parseInt(redisService.get(token));
		serviceOnline.setUserId(userId);
		int i=serviceOnlineService.addPic(serviceOnline);
		FinalResult finalResult = new FinalResult();
		if(i>0) {
			finalResult.setCode("100");
			finalResult.setMsg("发送成功");
		}else{
			finalResult.setCode("104");
			finalResult.setMsg("发送失败");
		}
		return finalResult;
	}
	
	
	
}
