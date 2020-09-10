package com.jizhi.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jizhi.pojo.FinalResult;
import com.jizhi.pojo.vo.FeedSendParam;
import com.jizhi.pojo.vo.FeedVO;
import com.jizhi.service.FeedService;
import com.jizhi.util.RedisService;

@RestController
@RequestMapping("feed")
public class FeedController {
	@Autowired
	private FeedService feedService;
	@Autowired
	private RedisService  redisService;
	
	/**
	 * 查询用户当前所有饲料
	 */
	@RequestMapping("queryTotal")
	public FinalResult queryTotalFeed(HttpServletRequest request) {
		FinalResult finalResult = new FinalResult();
		String token=request.getHeader("token");
		String user_id=redisService.get(token);
		Integer userId = Integer.parseInt(user_id);
		Double totalFeed=feedService.queryTotalFeed(userId);
		finalResult.setCode("100"); 
		finalResult.setBody(totalFeed);
		return finalResult;
	}
	
	
	/**
	 * 查询用户当前所有饲料
	 */
	@RequestMapping("queryDetail")
	public FinalResult queryFeedDetail(HttpServletRequest request) {
		FinalResult finalResult = new FinalResult();
		String token=request.getHeader("token");
		String user_id=redisService.get(token);
		Integer userId = Integer.parseInt(user_id);
		List<FeedVO> list=feedService.queryFeedDetail(userId);
		finalResult.setCode("100"); 
		finalResult.setBody(list);
		return finalResult;
	}
	
	
	/**
	 *转赠饲料
	 */
	@RequestMapping("send")
	public FinalResult sendFeed(HttpServletRequest request,@RequestBody FeedSendParam feedSendParam) {
		FinalResult finalResult = new FinalResult();
		String token=request.getHeader("token");
		String user_id=redisService.get(token);
		Integer userId = Integer.parseInt(user_id);
		feedSendParam.setUserId(userId);
		String result=feedService.sendFeed(feedSendParam);
		if(StringUtils.isBlank(result)) {
			finalResult.setCode("100"); 
			finalResult.setMsg("转赠成功");
		}else {
			finalResult.setCode("101"); 
			finalResult.setMsg(result);
		}
		return finalResult;
	}
	
}
