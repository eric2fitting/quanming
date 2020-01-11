package com.jizhi.util;

import org.springframework.stereotype.Component;

@Component
public class RandomStrUtil {
	
	
	public String getRandomString2(int length){
	    StringBuffer sb=new StringBuffer();
	    for(int i=0;i<length;i++){
	    	long result=0;
	    	result=Math.round(Math.random()*25+97);
	    	sb.append(String.valueOf((char)result));
	     }
	     return sb.toString();
	 }
}
