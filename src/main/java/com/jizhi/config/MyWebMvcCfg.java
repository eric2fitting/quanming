package com.jizhi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.jizhi.interceptor.LoginInterceptor;
import com.jizhi.interceptor.OrderInterceptor;

@Configuration
public class MyWebMvcCfg extends WebMvcConfigurerAdapter{
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		/*
		 * registry.addInterceptor(new LoginInterceptor()) .addPathPatterns("/**")
		 * .excludePathPatterns("/user/login","/user/register","/user/updatePsw",
		 * "/user/code");
		 */

		registry.addInterceptor(new OrderInterceptor())
		.addPathPatterns("/order/*");
	}
}
