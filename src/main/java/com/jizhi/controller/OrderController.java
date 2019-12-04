package com.jizhi.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jizhi.pojo.FinalResult;
import com.jizhi.pojo.Order;
import com.jizhi.pojo.vo.OrderDetail;
import com.jizhi.service.OrderService;

@RestController
@RequestMapping("order")
public class OrderController {
	
	
	@Autowired OrderService orderService;
	
	
	
	/**
	 * 添加预约
	 * @param order
	 * @return
	 */
	@RequestMapping("/add")
	public FinalResult addOrder(@RequestBody Order order) {
		int i=this.orderService.addOrder(order);
		FinalResult finalResult = new FinalResult();
		if(i>0) {
			finalResult.setCode("100");
			finalResult.setMsg("预约中");
			
		}else {
			finalResult.setCode("104");
			finalResult.setMsg("无法预约");
		}
		return finalResult;
	}
	
	
	/**
	 * 尝试预约
	 * @param request
	 * @return
	 */
	@RequestMapping("/try")
	public FinalResult toOrder(HttpServletRequest request) {
		String animal_Id = request.getParameter("animalId");
		Integer animalId=Integer.parseInt(animal_Id);
		String token = "token";
		request.getHeader(token);
		List<Integer> list=this.orderService.toOrder(animalId,token);
		FinalResult finalResult = new FinalResult();
		finalResult.setCode("100");
		finalResult.setBody(list);
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
