package com.jizhi.interceptor;

import com.jizhi.util.RedisService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class LoginInterceptor implements HandlerInterceptor{
	
	@Autowired 
	private RedisService redisService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String token = request.getHeader("token");
		//token为空说明未登录
		if(StringUtils.isEmpty(token)) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			PrintWriter out = response.getWriter();
			JSONObject res = new JSONObject();
			res.put("code", "104");
			res.put("msg", "未登陆");
			out.append(res.toString());
			return false;
		}else {
			String userId =redisService.get(token);
			//token过期
			if(StringUtils.isEmpty(userId)) {
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json; charset=utf-8");
				PrintWriter out = response.getWriter();
				JSONObject res = new JSONObject();
				res.put("code", "101");
				res.put("msg", "登陆失效");
				out.append(res.toString());
				return false;
			}else {
				return true;
			}
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}
	
}
