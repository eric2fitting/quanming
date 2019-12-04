package com.jizhi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jizhi.pojo.FinalResult;
import com.jizhi.pojo.vo.ShowInfo;
import com.jizhi.service.AnimalService;

@RestController
@RequestMapping("animal")
public class AnimalController {
	
	@Autowired
	private AnimalService animalService;
	
	
	/**
	 * 展示首页可预约的动物及时间列表信息
	 * @return
	 */
	@RequestMapping("/all")
	public FinalResult queryAnimalList() {
		List<ShowInfo> list=this.animalService.queryAnimalList();
		FinalResult finalResult = new FinalResult();
		if(list==null) {
			finalResult.setBody(null);
			finalResult.setMsg("暂无可喂养动物");
			finalResult.setCode("104");
		}else {
			finalResult.setBody(list);
			finalResult.setMsg("可喂养动物列表");
			finalResult.setCode("100");
		}
		return finalResult;
	}
	
	
}
