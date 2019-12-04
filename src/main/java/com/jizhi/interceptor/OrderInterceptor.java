package com.jizhi.interceptor;

/**
 * 预约领养前的拦截，需要绑定身份证和银行卡成功
 */

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.jizhi.dao.UserDao;
import com.jizhi.pojo.User;
import com.jizhi.util.RedisService;
import net.sf.json.JSONObject;

public class OrderInterceptor implements HandlerInterceptor{
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private UserDao userDao;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//拦截未身份认证的人或者被冻结的人
		String token=request.getHeader("token");
		String userId = this.redisService.get(token);
		Integer id=Integer.parseInt(userId);
		User user=this.userDao.queryById(id);
		Integer isConfirmed = user.getIsConfirmed();
		if(isConfirmed==0) {
			//表示还没有认证身份和银行信息
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			PrintWriter out = response.getWriter();
			JSONObject res = new JSONObject();
			res.put("code", "104");
			res.put("msg", "身份证银行卡未绑定或未认证成功");
			out.append(res.toString());
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
