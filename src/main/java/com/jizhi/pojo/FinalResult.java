package com.jizhi.pojo;

import java.io.Serializable;

public class FinalResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//状态码  100:成功；101登陆信息
	private String code;
	//返回信息
	private String msg;
	//返回的具体内容
	private Object body;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
	
}
