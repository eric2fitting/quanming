package com.jizhi.pojo;

import java.io.Serializable;

/**
 * 身份证信息类
 * @author Administrator
 *
 */
public class IdCard implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer userId;
	private String name;
	private String pic;
	private String idNum;
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}

}
