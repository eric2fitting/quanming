package com.jizhi.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jizhi.pojo.FinalResult;
import com.jizhi.pojo.IdCard;
import com.jizhi.service.IdCardService;

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
		if(i>0) {
			fz.setCode("100"); 
			fz.setMsg("保存成功");
		}else {
			fz.setCode("104"); 
			fz.setMsg("保存失败");
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
	
	
}
