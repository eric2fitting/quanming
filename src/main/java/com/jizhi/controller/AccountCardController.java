package com.jizhi.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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
	public FinalResult saveIdCard(@RequestBody AccountCard accountCard,HttpServletRequest request) {
		FinalResult finalResult = new FinalResult();
		String token = request.getHeader("token");
		int i=this.accountCardService.save(accountCard,token);
		if(i>0) {
			finalResult.setCode("100");
			finalResult.setMsg("添加成功");
		}else {
			finalResult.setCode("104");
			finalResult.setMsg("添加失败");
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
	public FinalResult updateIdCard(@RequestBody AccountCard accountCard,HttpServletRequest request) {
		FinalResult finalResult = new FinalResult();
		String token = request.getHeader("token");
		int i=this.accountCardService.updateIdCard(accountCard,token);
		if(i>0) {
			finalResult.setCode("100");
			finalResult.setMsg("更新成功");
		}else {
			finalResult.setCode("104");
			finalResult.setMsg("更新失败");
		}
		return finalResult;
	}
	@RequestMapping("/queryAll")
	public FinalResult queryAll(HttpServletRequest request) {
		String token = request.getHeader("token");
		List<AccountCard> list=accountCardService.queryAll(token);
		FinalResult finalResult = new FinalResult();
		finalResult.setCode("100");
		finalResult.setBody(list);
		return finalResult;
	}
	
	
	

}
