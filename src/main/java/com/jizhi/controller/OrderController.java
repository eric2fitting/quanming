package com.jizhi.controller;

import com.jizhi.pojo.Animal;
import com.jizhi.pojo.FinalResult;
import com.jizhi.pojo.OrderTime;
import com.jizhi.pojo.vo.AnimaInfo;
import com.jizhi.pojo.vo.OrderDetail;
import com.jizhi.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("order")
public class OrderController {
	
	
	@Autowired 
	private OrderService orderService;
	
	
	
	/**
	 * 添加预约
	 * @param order
	 * @return
	 */
	@RequestMapping("/add")
	public FinalResult addOrder(@RequestBody OrderTime orderTime,HttpServletRequest request) {
		//预约时间表的id
		Integer id = orderTime.getId();
		String token = request.getHeader("token");
		int i=this.orderService.addOrder(id,token);
		FinalResult finalResult = new FinalResult();
		if(i==1) {
			finalResult.setCode("100");
			finalResult.setMsg("预约中");
			
		}else if(i==2){
			finalResult.setCode("104");
			finalResult.setMsg("饲料不足");
		}
		return finalResult;
	}
	
	
	/**
	 * 尝试预约
	 * @param request
	 * @return
	 */
	@RequestMapping("/try")
	public FinalResult toOrder(@RequestBody Animal animal, HttpServletRequest request) {
		Integer animalId = animal.getId();
		String token = request.getHeader("token");
		AnimaInfo animaInfo=this.orderService.toOrder(animalId,token);
		FinalResult finalResult = new FinalResult();
		finalResult.setCode("100");
		finalResult.setBody(animaInfo);
		return finalResult;
	}
	
	
	/**
	 * 该用户所有预约信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/allOrder")
	public FinalResult queryAllByUserId(HttpServletRequest request) {
		String token = request.getHeader("token");
		List<OrderDetail> list=orderService.queryAllByUserId(token);
		FinalResult finalResult = new FinalResult();
		finalResult.setCode("100");
		finalResult.setBody(list);
		return finalResult;
	}
}
