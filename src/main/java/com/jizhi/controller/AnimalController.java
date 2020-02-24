package com.jizhi.controller;

import com.jizhi.pojo.FinalResult;
import com.jizhi.pojo.vo.ShowInfo;
import com.jizhi.service.AnimalService;
import com.jizhi.service.FirstPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("animal")
public class AnimalController {
	
	@Autowired
	private AnimalService animalService;
	@Autowired
	private FirstPageService firstPageService;
	
	
	/**
	 * 展示首页可预约的动物及时间列表信息  呵呵 嘻嘻
	 * bbb
	 * @return
	 */
	@RequestMapping("/all")
	public FinalResult queryAnimalList() {
		List<ShowInfo> list=this.animalService.queryAnimalList();
		FinalResult finalResult = new FinalResult();
		if(list.size()==0) {
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
	
	/**
	 *第一条标题
	 * @return
	 */
	@RequestMapping("/firstPage")
	public FinalResult firstPageInfo() {
		HashMap<String, Object> map= firstPageService.queryfirstPageInfo();
		FinalResult finalResult = new FinalResult();
		finalResult.setCode("100");
		finalResult.setBody(map);
		return finalResult;
	}
	
	public String test() {
		return "hehe";
	}
	
}
