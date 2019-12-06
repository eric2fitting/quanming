package com.jizhi.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jizhi.pojo.FinalResult;
import com.jizhi.pojo.vo.FeedingDetail;
import com.jizhi.service.PropertyService;

@RestController
@RequestMapping("property")
public class PropertyController {
	
	@Autowired
	private PropertyService propertyService;
	
	
	@RequestMapping("/waitSellList")
	public FinalResult waitSell(HttpServletRequest request) {
		String token = request.getHeader("token");
		List<FeedingDetail> list=propertyService.queryWaitSellList(token);
		FinalResult finalResult = new FinalResult();
		finalResult.setCode("100");
		finalResult.setBody(list);
		return finalResult;
	}
	
	@RequestMapping("/isSellingList")
	public FinalResult isSelling(HttpServletRequest request) {
		String token = request.getHeader("token");
		List<FeedingDetail> list=propertyService.queryIsSellingList(token);
		FinalResult finalResult = new FinalResult();
		finalResult.setCode("100");
		finalResult.setBody(list);
		return finalResult;
	}
	
	@RequestMapping("/isSoldList")
	public FinalResult isSold(HttpServletRequest request) {
		String token = request.getHeader("token");
		List<FeedingDetail> list=propertyService.queryIsSoldList(token);
		FinalResult finalResult = new FinalResult();
		finalResult.setCode("100");
		finalResult.setBody(list);
		return finalResult;
	}
	
}
