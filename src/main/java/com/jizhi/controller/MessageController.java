package com.jizhi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jizhi.pojo.FinalResult;
import com.jizhi.pojo.Message;
import com.jizhi.service.MessageService;

@RequestMapping("message")
@RestController
public class MessageController {
	@Autowired
	private MessageService messageService;
	
	
	@RequestMapping("/all")
	public FinalResult showAllMessage() {
		List<Message> list=this.messageService.queryAll();
		FinalResult finalResult = new FinalResult();
		finalResult.setCode("100");
		finalResult.setBody(list);
		return finalResult;
	}
	
	@RequestMapping("/part")
	public FinalResult showMessage(@RequestBody Message message) {
		List<Message> list=this.messageService.queryByType(message.getType());
		FinalResult finalResult = new FinalResult();
		finalResult.setCode("100");
		finalResult.setBody(list);
		return finalResult;
	}
	
	@RequestMapping("/save")
	public FinalResult addMessage(@RequestBody Message message) {
		Integer i=this.messageService.save(message);
		FinalResult finalResult = new FinalResult();
		if(i>0) {
			finalResult.setCode("100");
			finalResult.setMsg("保存成功");
		}else {
			finalResult.setCode("104");
			finalResult.setMsg("保存失败");
		}
		return finalResult;
	}
}