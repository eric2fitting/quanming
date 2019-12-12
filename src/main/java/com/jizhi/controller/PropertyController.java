package com.jizhi.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jizhi.pojo.FinalResult;
import com.jizhi.pojo.Match;
import com.jizhi.pojo.vo.FeedingDetail;
import com.jizhi.pojo.vo.SellInfo;
import com.jizhi.service.PropertyService;

@RestController
@RequestMapping("property")
public class PropertyController {
	
	@Autowired
	private PropertyService propertyService;
	
	
	/**
	 * 等待转让
	 * @param request
	 * @return
	 */
	@RequestMapping("/waitSellList")
	public FinalResult waitSell(HttpServletRequest request) {
		String token = request.getHeader("token");
		List<FeedingDetail> list=propertyService.queryWaitSellList(token);
		FinalResult finalResult = new FinalResult();
		finalResult.setCode("100");
		finalResult.setBody(list);
		return finalResult;
	}
	
	/**
	 * 正在转让
	 * @param request
	 * @return
	 */
	@RequestMapping("/isSellingList")
	public FinalResult isSelling(HttpServletRequest request) {
		String token = request.getHeader("token");
		List<FeedingDetail> list=propertyService.queryIsSellingList(token);
		FinalResult finalResult = new FinalResult();
		finalResult.setCode("100");
		finalResult.setBody(list);
		return finalResult;
	}
	
	
	/**
	 * 转让完成
	 * @param request
	 * @return
	 */
	@RequestMapping("/isSoldList")
	public FinalResult isSold(HttpServletRequest request) {
		String token = request.getHeader("token");
		List<FeedingDetail> list=propertyService.queryIsSoldList(token);
		FinalResult finalResult = new FinalResult();
		finalResult.setCode("100");
		finalResult.setBody(list);
		return finalResult;
	}
	
	/**
	 * 确认转让页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/toSell")
	public FinalResult toSell(@RequestBody Match  match) {
		SellInfo sellInfo=propertyService.toSell(match.getId());
		FinalResult finalResult = new FinalResult();
		finalResult.setCode("100");
		finalResult.setBody(sellInfo);
		return finalResult;
	}
	
	/**
	 * 确认转让
	 * @param request
	 * @return
	 */
	@RequestMapping("/doSell")
	public FinalResult doSell(@RequestBody Match  match) {
		propertyService.doSell(match.getId());
		FinalResult finalResult = new FinalResult();
		finalResult.setCode("100");
		finalResult.setMsg("转让成功");
		return finalResult;
		
	}
	
}
