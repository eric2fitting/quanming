package com.jizhi.controller;

import com.jizhi.pojo.FinalResult;
import com.jizhi.pojo.vo.FeedExchangeParam;
import com.jizhi.pojo.vo.ShareProfitsVO;
import com.jizhi.pojo.vo.UserInfo;
import com.jizhi.service.ProfitsService;
import com.jizhi.util.RedisService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.servlet.http.HttpServletRequest;


@RequestMapping("profits")
@RestController
public class ProfitsController {
	
	@Autowired
	private ProfitsService profitsService;
	@Autowired
	private RedisService redisService;
	/**
	 * 提现
	 * @param request
	 * @param userInfo
	 * @return
	 */
	@RequestMapping("/update")
	public FinalResult updateShareProfit(HttpServletRequest request,@RequestBody UserInfo userInfo) {
		String token = request.getHeader("token");
		FinalResult finalResult = new FinalResult();
		if(userInfo.getShareProfit()%200!=0 || userInfo.getShareProfit()<200) {
			finalResult.setCode("104");
			finalResult.setMsg("提现金额必须是200的倍数");
			return finalResult;
		}
		Integer i=profitsService.updateShareProfit(token,userInfo);
		if(i>0) {
			finalResult.setCode("100");
		}else {
			finalResult.setCode("104");
			finalResult.setMsg("余额不足");
		}
		return finalResult;
	}
	
	/**
	 *用分享收益兑换饲料页面显示
	 * @param request
	 * @return
	 */
	@RequestMapping("/tryShareProfitsToFeed")
	public FinalResult tryShareProfitsToFeed(HttpServletRequest request) {
		String token = request.getHeader("token");
		String user_id = redisService.get(token);
		Integer userId=Integer.parseInt(user_id);
		FeedExchangeParam result=profitsService.tryShareProfitsToFeed(userId);
		FinalResult finalResult = new FinalResult();
		finalResult.setCode("100");
		finalResult.setBody(result);
		return finalResult;
	}
	
	/**
	 *分享收益兑换饲料接口
	 * @param request
	 * @return
	 */
	@RequestMapping("/shareProfitsToFeed")
	public FinalResult shareProfitsToFeed(@RequestBody FeedExchangeParam feedExchangeParam) {
		Integer i=profitsService.shareProfitsToFeed(feedExchangeParam);
		FinalResult finalResult = new FinalResult();
		if(i==0) {
			finalResult.setCode("101");
			finalResult.setMsg("超过最大兑换数量");
		}else if (i==1) {
			finalResult.setCode("101");
			finalResult.setMsg("二级密码错误");
		}else if (i==2) {
			finalResult.setCode("100");
			finalResult.setMsg("兑换成功");
		}else if (i==3) {
			finalResult.setCode("101");
			finalResult.setMsg("会员等级达到v2才能兑换");
		}
		return finalResult;
	}
	
//	客户暂时取消NFC（mtoken）兑换饲料（金券）
//	/**
//	 *用NFC币兑换饲料页面显示
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping("/tryNFCToFeed")
//	public FinalResult tryNFCToFeed(HttpServletRequest request) {
//		String token = request.getHeader("token");
//		String user_id = redisService.get(token);
//		Integer userId=Integer.parseInt(user_id);
//		FeedExchangeParam result=profitsService.tryNFCToFeed(userId);
//		FinalResult finalResult = new FinalResult();
//		finalResult.setCode("100");
//		finalResult.setBody(result);
//		return finalResult;
//	}
//	
//	/**
//	 *NFC兑换饲料接口
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping("/NFCToFeed")
//	public FinalResult NFCToFeed(@RequestBody FeedExchangeParam feedExchangeParam) {
//		Integer i=profitsService.NFCToFeed(feedExchangeParam);
//		FinalResult finalResult = new FinalResult();
//		if(i==0) {
//			finalResult.setCode("101");
//			finalResult.setMsg("超过最大兑换数量");
//		}else if (i==1) {
//			finalResult.setCode("101");
//			finalResult.setMsg("二级密码错误");
//		}else if (i==2) {
//			finalResult.setCode("100");
//			finalResult.setMsg("兑换成功");
//		}
//		return finalResult;
//	}
//	
	/**
	 * 提现列表
	 * @return
	 */
	@RequestMapping("/shareProfitsList")
	public FinalResult shareProfitsList(HttpServletRequest request) {
		FinalResult finalResult = new FinalResult();
		List<ShareProfitsVO> responce=profitsService.queryShareProfitsList(request.getHeader("token"));
		finalResult.setCode("100");
		finalResult.setBody(responce);
		return finalResult;
	}
}
