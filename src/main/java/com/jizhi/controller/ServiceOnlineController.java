package com.jizhi.controller;

import com.jizhi.pojo.FinalResult;
import com.jizhi.pojo.ServiceOnline;
import com.jizhi.pojo.User;
import com.jizhi.service.ServiceOnlineService;
import com.jizhi.util.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
	
	
	@RequestMapping("/allNotAnswered")
	public FinalResult queryNotAnswered() {
		List<User> list=serviceOnlineService.queryNotAnswered();
		FinalResult finalResult = new FinalResult();
		finalResult.setCode("100");
		finalResult.setBody(list);
		return finalResult;
	}
	
	/**
	 * 查看某人的问题
	 * @return
	 */
	@RequestMapping("/queryQue")
	public FinalResult queryQue(@RequestBody User user) {
		List<ServiceOnline> list = serviceOnlineService.queryByUserId(user.getId());
		FinalResult finalResult = new FinalResult();
		finalResult.setCode("100");
		finalResult.setBody(list);
		return finalResult;
	}
	
	@RequestMapping("/answer")
	public FinalResult answer(@RequestBody ServiceOnline serviceOnline ) {
		FinalResult finalResult = new FinalResult();
		int i=serviceOnlineService.addAnswer(serviceOnline);
		if(i>0) {
			finalResult.setCode("100");
		}else {
			finalResult.setCode("104");
		}
		return finalResult;
	}
}
