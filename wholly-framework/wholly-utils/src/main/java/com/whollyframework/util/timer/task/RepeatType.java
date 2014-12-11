package com.whollyframework.util.timer.task;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.quartz.CronExpression;

public class RepeatType {
	/** 每天 */
	public static final int DAILY = 0x0000002;

	/** 每分 */
	public static final int DAILY_MINUTES = 0x0000022;

	/** 每个工作日 */
	public static final int WORKDAY = 0x000021;

	/** 每周 */
	public static final int WEEKLY = 0x0000020;

	/** 每时 */
	public static final int DAILY_HOURS = 0x0000222;

	/** 每月 */
	public static final int MONTHLY = 0x0000200;

	/** 立刻 */
	public static final int IMMEDIATE = 0x0002000;

	/** 每年 */
	public static final int YEAR = 0x0200000;

	/** 不重复 */
	public static final int NONE = 0;

	public static String buildCronExpression(int repeatType, int repeatFrequency, Date firstDate) {
		Calendar calendar = Calendar.getInstance();

		int second = calendar.get(Calendar.SECOND);
		int minute = calendar.get(Calendar.MINUTE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int days = calendar.get(Calendar.DAY_OF_MONTH);

		String rtn = "";

		switch (repeatType) {
		case (RepeatType.NONE):
			// 不重复
			break;
		case (RepeatType.DAILY):
			// 每天
			rtn = second + " " + minute + " " + hour + " 1/" + repeatFrequency + " * ?";
			break;
		case (RepeatType.WEEKLY):
			rtn = second + " " + minute + " " + hour + " ? * 1/" + repeatFrequency;
			break;
		case (RepeatType.WORKDAY):
			rtn = second + " " + minute + " " + hour + " ? * " + "2,3,4,5,6/" + repeatFrequency;
			break;
		case (RepeatType.MONTHLY):
			// 每月
			rtn = second + " " + minute + " " + hour + " " + days + " 1/" + repeatFrequency + " ?";
			break;
		case (RepeatType.IMMEDIATE):
			// 立刻
			break;
		case (RepeatType.DAILY_MINUTES):
			// 每分
			rtn = "0 1/" + repeatFrequency + " * * * ?";
			break;
		case (RepeatType.DAILY_HOURS):
			// 每小时
			rtn = "0 0 1/" + repeatFrequency + " * * ?";
			break;
		}

		return rtn;
	}

	public static Date getNextDate(int repeatType, int repeatFrequency, Date firstDate) throws ParseException {
		String expression = buildCronExpression(repeatType, repeatFrequency, firstDate);
//		System.out.println("Expression: " + expression);

		CronExpression expr = new CronExpression(expression);
		Date next = expr.getNextValidTimeAfter(firstDate);

//		System.out.println("Current Date : " + ConvertUtil.format(firstDate, "yyyy-MM-dd HH:mm:ss"));
//		System.out.println("Next Date : " + ConvertUtil.format(next, "yyyy-MM-dd HH:mm:ss"));

		return next;
	}

	/**
	 * 重复类型
	 */
	public static Map<Integer, String> REPROTTYPE_MAP = new TreeMap<Integer, String>();
	static {
		REPROTTYPE_MAP.put(DAILY, "每天");
		REPROTTYPE_MAP.put(DAILY_MINUTES, "每分");
		REPROTTYPE_MAP.put(WORKDAY, "每个工作日");
		REPROTTYPE_MAP.put(WEEKLY, "每周");
		REPROTTYPE_MAP.put(DAILY_HOURS, "每小时");
		REPROTTYPE_MAP.put(MONTHLY, "每月");
		REPROTTYPE_MAP.put(IMMEDIATE, "立刻");
		REPROTTYPE_MAP.put(YEAR, "每年");
		REPROTTYPE_MAP.put(NONE, "不重复");
	}
}
