package com.jizhi.controller;

import com.jizhi.pojo.FinalResult;
import com.jizhi.pojo.IdCard;
import com.jizhi.service.IdCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("idCard")
public class IdCardController {
	@Autowired
	private IdCardService idCardService;

	
	@RequestMapping("/save")
	public FinalResult saveIdCard(@RequestBody IdCard idCard,
			HttpServletRequest request) {
		String token = request.getHeader("token");
		int i=idCardService.saveIdCard(idCard,token);
		FinalResult fz = new FinalResult();
		if(i==1) {
			fz.setCode("100"); 
			fz.setMsg("保存成功");
		}else if (i==2) {
			fz.setCode("104"); 
			fz.setMsg("身份证号码重复");
		}else if (i==3) {
			fz.setCode("104"); 
			fz.setMsg("请先绑定收益方式");
		}else if (i==4) {
			fz.setCode("104"); 
			fz.setMsg("请勿重复认证");
		}
		else {
			fz.setCode("104"); 
			fz.setMsg("系统错误");
		}
		return fz;
	}

	
	
	@RequestMapping("/update")
	public FinalResult updateIdCard(@RequestBody IdCard idCard,
			HttpServletRequest request) {
		String token = request.getHeader("token");
		int i=idCardService.updateIdCard(idCard,token);
		FinalResult fz = new FinalResult();
		if(i>0) {
			fz.setCode("100"); 
			fz.setMsg("更新成功");
		}else {
			fz.setCode("104"); 
			fz.setMsg("更新失败");
		}
		return fz;
	}
	
	
	@RequestMapping("/isConfirmed")
	public FinalResult isConfirmed(HttpServletRequest request) {
		String token = request.getHeader("token");
		Integer integer=idCardService.queryIsConfirmed(token);
		FinalResult finalResult = new FinalResult();
		finalResult.setCode("100");
		finalResult.setBody(integer);
		return finalResult;
	}
	
	
}
