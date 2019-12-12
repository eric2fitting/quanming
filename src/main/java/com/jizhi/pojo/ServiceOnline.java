package com.jizhi.pojo;

import java.io.Serializable;

public class ServiceOnline implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer userId;
	private String que;
	private String pic;
	private String answer;
	private Integer isAnswered;
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
	public String getQue() {
		return que;
	}
	public void setQue(String que) {
		this.que = que;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public Integer getIsAnswered() {
		return isAnswered;
	}
	public void setIsAnswered(Integer isAnswered) {
		this.isAnswered = isAnswered;
	}
	
}
