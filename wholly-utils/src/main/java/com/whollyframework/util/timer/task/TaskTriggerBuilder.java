package com.whollyframework.util.timer.task;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import com.whollyframework.constans.TaskConstants;
import com.whollyframework.util.DateUtil;
import com.whollyframework.util.StringUtil;

public class TaskTriggerBuilder {
	private Date startTime; // 任务开始时间

	private int period; // 重复类型

	private String group = TaskConstants.TASK_GROUP;

	private String name = "Task";

	/**
	 * 每周中的星期几
	 */
	Integer[] daysOfWeek;

	/**
	 * 每周中第几天
	 */
	int dayOfMonth;

	Calendar calendar = Calendar.getInstance();

	int second;
	int minute;
	int hour;
	int days;
	int month;

	public TaskTriggerBuilder() {
	}

	public void addStartTime(Date startTime) {
		second = calendar.get(Calendar.SECOND);
		minute = calendar.get(Calendar.MINUTE);
		hour = calendar.get(Calendar.HOUR_OF_DAY);
		days = calendar.get(Calendar.DAY_OF_MONTH);
		month = calendar.get(Calendar.MONTH) + 1;

		this.startTime = startTime;
	}

	public void addPeriod(int period) {
		this.period = period;
	}

	public void addDaysOfWeek(Integer[] daysOfWeek) {
		this.daysOfWeek = daysOfWeek;
	}

	public void addDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public void addName(String name) {
		this.name = name;
	}

	/**
	 * 解析task并设置相关属性
	 * 
	 * @param task
	 */
	public void addTask(Task task) {
		addName(task.getId());
		addPeriod(task.getPeriod());
		addStartTime(task.getRunningTime());
		if (task.getDaysOfWeek() != null) {
			addDaysOfWeek(task.getDaysOfWeek().toArray(new Integer[task.getDaysOfWeek().size()]));
		}
		addDayOfMonth(task.getDayOfMonth());
	}

	/**
	 * 生成Cron表达式
	 * 
	 * @return
	 * @throws ParseException
	 */
	public String buildCronExpression() throws ParseException {
		String rtn = "";

		switch (period) {
		case (RepeatType.NONE):
			// 不重复
			break;
		case (RepeatType.DAILY):
			// 每天
			rtn = second + " " + minute + " " + hour + " * * ?";
			break;
		case (RepeatType.WEEKLY):
			rtn = second + " " + minute + " " + hour + " ? * " + StringUtil.join(daysOfWeek, ",");
			break;
		case (RepeatType.WORKDAY):
			rtn = second + " " + minute + " " + hour + " ? * " + "2,3,4,5,6";
			break;
		case (RepeatType.MONTHLY):
			// 每月
			rtn = second + " " + minute + " " + hour + " " + days + " * ?";
			break;
		case (RepeatType.IMMEDIATE):
			// 立刻
			break;
		case (RepeatType.DAILY_MINUTES):
			// 每分
			rtn = "0 * * * * ?";
			break;
		case (RepeatType.DAILY_HOURS):
			// 每小时
			rtn = "0 0 * * * ?";
			break;
		}

//		CronExpression expr = new CronExpression(rtn);
//		Date next = new Date();
//		for (int i = 0; i < 50; i++) {
//			next = expr.getNextValidTimeAfter(next);
//			System.out.println("Next Date " + i + ": " + ConvertUtil.format(next, "yyyy-MM-dd HH:mm:ss"));
//		}

		return rtn;
	}

	public Trigger build() throws ParseException {
		Trigger trigger = null;

		switch (period) {
		case (TaskConstants.REPEAT_TYPE_NONE):
			// 不重复
			trigger = TriggerBuilder.newTrigger().withIdentity(name, group).startAt(startTime).build();
			break;
		case (TaskConstants.REPEAT_TYPE_IMMEDIATE):
			// 立刻
			trigger = TriggerBuilder.newTrigger().withIdentity(name, group).startNow().build();
			break;
		default:
			// 每天
			trigger = TriggerBuilder.newTrigger().withIdentity(name, group)
					.withSchedule(CronScheduleBuilder.cronSchedule(buildCronExpression())).build();
			break;
		}

		return trigger;
	}

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		TaskTriggerBuilder builder = new TaskTriggerBuilder();
		Date start = new Date();
		builder.addStartTime(start);
		builder.addPeriod(RepeatType.WORKDAY);
		builder.addDaysOfWeek(new Integer[] { 2, 3, 4 });
		builder.addDayOfMonth(10);
		System.out.println(builder.buildCronExpression());
		try {
			System.out.println(DateUtil.format(start, "yyyy-MM-dd HH:mm:ss") + " -> " + builder.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Trigger trigger = builder.build();
		System.out.println(trigger.getKey());

	}
}
