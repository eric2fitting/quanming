package com.jizhi.pojo.vo;

import com.jizhi.pojo.Animal;
import com.jizhi.pojo.OrderTime;

import java.io.Serializable;
import java.util.List;

public class ShowInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Animal animal;
	private List<OrderTime> list;
	public Animal getAnimal() {
		return animal;
	}
	public void setAnimal(Animal animal) {
		this.animal = animal;
	}
	public List<OrderTime> getList() {
		return list;
	}
	public void setList(List<OrderTime> list) {
		this.list = list;
	}
	
}
