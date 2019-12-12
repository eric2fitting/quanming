package com.jizhi.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jizhi.pojo.FinalResult;
import com.jizhi.pojo.Match;
import com.jizhi.pojo.vo.BuyerDoPay;
import com.jizhi.pojo.vo.BuyingDetail;
import com.jizhi.pojo.vo.FeedingDetail;
import com.jizhi.pojo.vo.PayInfo;
import com.jizhi.service.MatchService;


@RequestMapping("user")
@RestController
public class MatchController {
	
	@Autowired
	private MatchService matchService;
	
	
	@RequestMapping("/allBuying")
	public FinalResult showAllBuying(HttpServletRequest request) {
		String token=request.getHeader("token");
		List<BuyingDetail> list=matchService.showAllBuying(token);
		FinalResult finalResult = new FinalResult();
		finalResult.setCode("100");
		finalResult.setBody(list);
		return finalResult;
	}
	
	@RequestMapping("/allFeeding")
	public FinalResult showAllFeeding(HttpServletRequest request) {
		String token=request.getHeader("token");
		List<FeedingDetail> list=matchService.showAllFeeding(token);
		FinalResult finalResult = new FinalResult();
		finalResult.setCode("100");
		finalResult.setBody(list);
		return finalResult;
	}
	
	
	/**
	 * 买家付款
	 * @param request
	 * @return
	 */
	@RequestMapping("/buyerToPay")
	public FinalResult buyerToPay(@RequestBody Match match) {
		Integer matchId = match.getId();
		PayInfo payInfo=matchService.buyerToPay(matchId);
		FinalResult finalResult = new FinalResult();
		finalResult.setCode("100");
		finalResult.setBody(payInfo);
		return finalResult;
	}
	
	/**
	 * 买家确认付款
	 * @param match
	 * @return
	 */
	@RequestMapping("/doPay")
	public FinalResult doPay(@RequestBody BuyerDoPay buyerDoPay,HttpServletRequest request) {
		String str=matchService.doPay(buyerDoPay,request);
		FinalResult finalResult = new FinalResult();
		finalResult.setCode("100");
		finalResult.setMsg(str);
		return finalResult;
	}
	
}
