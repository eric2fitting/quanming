package com.jizhi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.jizhi.interceptor.LoginInterceptor;
import com.jizhi.interceptor.OrderInterceptor;

@Configuration
public class MyWebMvcCfg extends WebMvcConfigurerAdapter{
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {


		registry.addInterceptor(getLoginInterceptor()) .addPathPatterns("/**")
		.excludePathPatterns("/qm/login","/qm/register",
		"/qm/code","/qm/renamePsw");

		registry.addInterceptor(new OrderInterceptor())
		.addPathPatterns("/order/*");
	}
	
	@Bean
	public HandlerInterceptor getLoginInterceptor(){
		return new LoginInterceptor(); 
		} 
}
