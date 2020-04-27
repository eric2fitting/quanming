package com.jizhi;

import java.math.BigDecimal;
import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;

public class TestApp {
	public static void main(String[] args) {
		ArrayList<City> list = new ArrayList<City>();
		list.add(new City(22.15D,1));
		list.add(new City(22.55D,0));
		list.add(new City(22.01D,1));
		list.add(new City(22.35D,0));
		Collections.sort(list, new Comparator<City>() {

			@Override
			public int compare(City o1, City o2) {
				Integer is1 = o1.getIs();
				Integer is2 = o2.getIs();
				int temp=is2.compareTo(is1);
				if(temp!=0) {
					return temp;
				}
				Double distance1 = o1.getDistance();
				Double distance2 = o2.getDistance();
				BigDecimal b1 = new BigDecimal(distance1);
				BigDecimal b2 = new BigDecimal(distance2);
				return b1.compareTo(b2);
			}
		});
		for(City city:list) {
			System.out.println(city.getIs()+":"+city.getDistance());
		}
	}
}
