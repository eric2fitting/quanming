package com.jizhi.controller;

import com.jizhi.pojo.FinalResult;
import com.jizhi.pojo.vo.UserInfo;
import com.jizhi.service.ProfitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RequestMapping("profits")
@RestController
public class ProfitsController {
	
	@Autowired
	private ProfitsService profitsService;
	
	
	@RequestMapping("/update")
	public FinalResult updateShareProfit(HttpServletRequest request,@RequestBody UserInfo userInfo) {
		String token = request.getHeader("token");
		Integer i=profitsService.updateShareProfit(token,userInfo);
		FinalResult finalResult = new FinalResult();
		if(i>0) {
			finalResult.setCode("100");
		}else {
			finalResult.setCode("104");
			finalResult.setMsg("体现金额要大于50且不能超过全部分享金额");
		}
		return finalResult;
	}
}
