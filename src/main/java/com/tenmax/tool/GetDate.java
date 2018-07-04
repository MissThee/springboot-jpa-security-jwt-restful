package com.tenmax.tool;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class GetDate {
	public static Date now(int i) {
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, i);// 把日期往后增加一天.整数往后推,负数往前移动
		return calendar.getTime(); // 这个时间就是日期往后推一天的结果
	}

	public static Date now() {
		return now(0);
	}

	/**
	 * @param fromDate
	 *            Date起始时间
	 * @param toDate
	 *            Date结束时间
	 * @param type
	 *            1-天数；2-小时；3-分钟；其他-秒
	 * @return int相差时间
	 */
	public static int dateDiff(Date fromDate, Date toDate, int type) {
		long from = fromDate.getTime();
		long to = toDate.getTime();
		int a = 1000;
		switch (type) {
		case 1:
			a = a * 60 * 60 * 24;
			break;
		case 2:
			a = a * 60 * 60;
			break;
		case 3:
			a = a * 60;
			break;
		default:
			break;
		}
		int time = (int) ((to - from) / a);
		return time;
	}

	/**
	 * @param fromDate
	 *            Date起始时间
	 * @param toDate
	 *            Date结束时间
	 * @return int相差秒数
	 * 
	 */
	public static int dateDiff(Date fromDate, Date toDate) {
		return dateDiff(fromDate, toDate);
	}
}
