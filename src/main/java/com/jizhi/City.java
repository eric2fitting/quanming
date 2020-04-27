package com.jizhi;

public class City {
	public Double getDistance() {
		return distance;
	}
	public void setDistance(Double distance) {
		this.distance = distance;
	}
	public Integer getIs() {
		return is;
	}
	public void setIs(Integer is) {
		this.is = is;
	}
	private Double distance;
	private Integer is;
	public City(Double distance, Integer is) {
		super();
		this.distance = distance;
		this.is = is;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}
