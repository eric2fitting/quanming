package com.jizhi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jizhi.pojo.FinalResult;
import com.jizhi.pojo.OtherInfo;
import com.jizhi.service.OtherInfoService;

@RestController
@RequestMapping("otherInfo")
public class OtherInfoController {
	
	
	@Autowired
	private OtherInfoService OtherInfoService;
	
	
	@RequestMapping("query")
	public FinalResult query(){
		OtherInfo otherInfo=OtherInfoService.query();
		FinalResult finalResult = new FinalResult();
		finalResult.setCode("100");
		finalResult.setBody(otherInfo);
		return finalResult;
	}
}
