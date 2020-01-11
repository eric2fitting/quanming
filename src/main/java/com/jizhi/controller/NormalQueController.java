package com.jizhi.controller;

import com.jizhi.pojo.FinalResult;
import com.jizhi.pojo.NormalQue;
import com.jizhi.service.NormalQueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("que")
public class NormalQueController {
	
	@Autowired
	private NormalQueService normalQueService;
	
	
	@RequestMapping("/add")
	public FinalResult addQue(@RequestBody NormalQue normalQue) {
		Integer integer=normalQueService.add(normalQue);
		FinalResult finalResult = new FinalResult();
		if(integer>0) {
			finalResult.setCode("100");
			finalResult.setMsg("添加成功");
		}else {
			finalResult.setCode("104");
			finalResult.setMsg("添加失败");
		}
		return finalResult;
		
	}
	
	@RequestMapping("/queryAll")
	public FinalResult queryAllQue() {
		List<NormalQue> list=normalQueService.queryAll();
		FinalResult finalResult = new FinalResult();
		finalResult.setCode("100");
		finalResult.setBody(list);
		return finalResult;
	}
	
	@RequestMapping("/delete")
	public FinalResult delete(@RequestBody NormalQue normalQue) {
		FinalResult finalResult = new FinalResult();
		int i=normalQueService.delete(normalQue.getId());
		if (i>0) {
			finalResult.setCode("100");
		}else {
			finalResult.setCode("104");
		}
		return finalResult;
	}
	
	@RequestMapping("/update")
	public FinalResult update(@RequestBody NormalQue normalQue){
		
		FinalResult finalResult = new FinalResult();
		int i=normalQueService.update(normalQue);
		if (i>0) {
			finalResult.setCode("100");
		}else {
			finalResult.setCode("104");
		}
		return finalResult;
	}
	
	
	
	
}
