package com.jizhi.pojo.vo;

import java.io.Serializable;
import java.util.List;

public class AnimaInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String animalType;
	private String animalSize;
	private List<IsOrderOrOverTime> list;
	private Double feedNeed;//预约需要的饲料
	private Double feedOwns;//拥有的

	public Double getFeedNeed() {
		return feedNeed;
	}
	public void setFeedNeed(Double feedNeed) {
		this.feedNeed = feedNeed;
	}
	public Double getFeedOwns() {
		return feedOwns;
	}
	public void setFeedOwns(Double feedOwns) {
		this.feedOwns = feedOwns;
	}
	public String getAnimalType() {
		return animalType;
	}
	public void setAnimalType(String animalType) {
		this.animalType = animalType;
	}
	public String getAnimalSize() {
		return animalSize;
	}
	public void setAnimalSize(String animalSize) {
		this.animalSize = animalSize;
	}

	
	public List<IsOrderOrOverTime> getList() {
		return list;
	}
	public void setList(List<IsOrderOrOverTime> list) {
		this.list = list;
	}
	
}
