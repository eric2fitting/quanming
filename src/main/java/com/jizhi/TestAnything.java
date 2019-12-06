package com.jizhi;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class TestAnything {
	public static void main(String[] args) {
		/*
		 * SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 * Calendar calendar = Calendar.getInstance();
		 * System.out.println(simpleDateFormat.format(calendar.getTime()));
		 * calendar.add(Calendar.DATE, 3);
		 * System.out.println(simpleDateFormat.format(calendar.getTime()));
		 */
	
		
	}
	public static void testList() {
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		System.out.println(arrayList.size());
	}
	
	public static void testRandom() {
		Random random = new Random();
		String string="";
		for(int i=0;i<6;i++) {
			int choice=random.nextInt(2);
			switch (choice) {
			case 0:
				int num=random.nextInt(10);
				string=string+num;
				break;
			case 1:
				char num2=(char) random.nextInt(25+97);
				string=string+num2;
				break;
			}
			
		}
		System.out.println(string);
	}
	
	public static void baoliuliangweixiaoshu() {
		double d= 50;
		for(int i=0;i<8;i++) {
			d=d *1.15;
			BigDecimal bigDecimal = new BigDecimal(d);
			d=bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
			System.out.println(d);
		}
	}
	
	public static void testGetNearestTime() {
		System.out.println("开始");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
		String s1 = "10:42";
		String s2 = "11:30";
		String s3 = "13:30";
		String s4 = "14:30";
		String s5 = "9:30";
		try {
			Date date1 = simpleDateFormat.parse(s1);
			Date date2 = simpleDateFormat.parse(s2);
			Date date3 = simpleDateFormat.parse(s3);
			Date date4 = simpleDateFormat.parse(s4);
			Date date5 = simpleDateFormat.parse(s5);
			Date nowDate=simpleDateFormat.parse(simpleDateFormat.format(new Date()));
			ArrayList<Date> list = new ArrayList<Date>();
			list.add(date1);
			list.add(date2);
			list.add(date3);
			list.add(date4);
			list.add(date5);
			
			//从list中取出即将到的时间
			int nearesti=0;
			for(int i=1;i<list.size();i++ ) {
				if(list.get(nearesti).after(nowDate)&&list.get(i).after(nowDate)) {
					if(list.get(nearesti).after(list.get(i))) {
						nearesti=i;
					}else if (list.get(nearesti).before(nowDate)&&list.get(i).after(nowDate)) {
						nearesti=i;
					}
				}
			}
			System.out.println("即将到的时间是："+list.get(nearesti));
			
		} catch (Exception e) {
			
		}
		
		
	}

	
}
