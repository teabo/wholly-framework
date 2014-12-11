package com.whollyframework.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Chris Xu
 * @since 2011-5-11 下午10:00:18
 */
public class CalendarUtil {

	public static Calendar getThisMonth(int year, int monthIndex) {
		Calendar thisMonth = Calendar.getInstance();
		if (year > 1900)
			thisMonth.set(Calendar.YEAR, year);
		else
			thisMonth.set(Calendar.YEAR, 1901);

		if (monthIndex >= 0 && monthIndex < 12)
			thisMonth.set(Calendar.MONTH, monthIndex);
		else
			thisMonth.set(Calendar.MONTH, 0);

		thisMonth.setFirstDayOfWeek(Calendar.SUNDAY);

		thisMonth.set(Calendar.DAY_OF_MONTH, 1);
		return thisMonth;
	}

	public Map<String, String> get_year() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		String stryear = format.format(new Date());
		long year = Long.parseLong(stryear);
		map.put(String.valueOf(year), year + "年");
		for (int i = 1; i < 5; i++) {
			map.put(String.valueOf(year - i), (year - i) + "年");
		}
		return map;
	}

	public Map<String, String> get_month() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (int i = 1; i < 13; i++) {
			if (i < 10)
				map.put("0" + i, i + "月");
			else
				map.put(String.valueOf(i), i + "月");
		}
		return map;
	}
}
