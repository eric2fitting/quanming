package com.jizhi.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jizhi.pojo.AccountCard;
import com.jizhi.pojo.FinalResult;
import com.jizhi.service.AccountCardService;


@RestController
@RequestMapping("accountCard")
public class AccountCardController {
	@Autowired
	private AccountCardService accountCardService;
	
	
	@RequestMapping("/save")
	public FinalResult saveIdCard(AccountCard accountCard,HttpServletRequest request) {
		FinalResult finalResult = new FinalResult();
		String token = request.getHeader("token");
		int i=this.accountCardService.save(accountCard,request,token);
		if(i>0) {
			finalResult.setCode("100");
			finalResult.setMsg("保存成功");
		}else {
			finalResult.setCode("104");
			finalResult.setMsg("保存失败");
		}
		return finalResult;
	}
	
	
	@RequestMapping("/del")
	public FinalResult delIdCard(HttpServletRequest request) {
		String token = request.getHeader("token");
		FinalResult finalResult = new FinalResult();
		int i=this.accountCardService.delIdCard(token);
		if(i>0) {
			finalResult.setCode("100");
			finalResult.setMsg("删除成功");
		}else {
			finalResult.setCode("104");
			finalResult.setMsg("删除失败");
		}
		return finalResult;
	}
	
	@RequestMapping("/update")
	public FinalResult updateIdCard(AccountCard accountCard,HttpServletRequest request) {
		FinalResult finalResult = new FinalResult();
		String token = request.getHeader("token");
		int i=this.accountCardService.updateIdCard(accountCard,request,token);
		if(i>0) {
			finalResult.setCode("100");
			finalResult.setMsg("更新成功");
		}else {
			finalResult.setCode("104");
			finalResult.setMsg("更新失败");
		}
		return finalResult;
	}

}
