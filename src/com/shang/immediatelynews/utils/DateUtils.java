package com.shang.immediatelynews.utils;

import java.util.Calendar;
import java.util.Date;

import android.util.Log;

public class DateUtils {

	public static String formatDate(Date date) {
		Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		int year = instance.get(Calendar.YEAR);
		int month = instance.get(Calendar.MONTH) + 1;
		int day = instance.get(Calendar.DATE);
		int hour = instance.get(Calendar.HOUR_OF_DAY);
		Calendar instance2 = Calendar.getInstance();
		if(year == instance2.get(Calendar.YEAR)) {
			if((month == instance2.get(Calendar.MONTH) + 1) && day == instance2.get(Calendar.DATE)) {
				int hour2 = instance2.get(Calendar.HOUR_OF_DAY) - hour;
				if(hour2 == 0) {
					return "刚刚";
				}else {
					return hour2 + "小时前";
				}
			}else if((month == instance2.get(Calendar.MONTH) + 1) && day < instance2.get(Calendar.DATE)){
				int day2 = instance2.get(Calendar.DATE) - day;
				return day2 + "天前";
			}
			return month + "月" + day + "日";
		}else {
			return year + "年" + month + "月" + day + "日";
		}
	}
}
