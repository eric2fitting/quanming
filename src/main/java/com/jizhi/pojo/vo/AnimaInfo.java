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

	private List<IsOrderOrOverTime> list;
	public List<IsOrderOrOverTime> getList() {
		return list;
	}
	public void setList(List<IsOrderOrOverTime> list) {
		this.list = list;
	}
	
}
