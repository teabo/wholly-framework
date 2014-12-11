package com.whollyframework.util.timer.task;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import com.whollyframework.base.model.ValueObject;
import com.whollyframework.constans.Environment;
import com.whollyframework.constans.TaskConstants;
import com.whollyframework.util.DateUtil;
import com.whollyframework.util.ObjectUtil;

/**
 * @author chris
 */
public abstract class Task extends ValueObject implements Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2407297137360225258L;

	private final static Logger LOG = Logger.getLogger(Task.class);

	private String description; // 描述

	private Date runningTime; // 开始时间

	private int period; // 重复类型

	private int runtimes; // 运行次数

	private int totalRuntimes; // 总运行次数

	private int state; // 状态

	private int startupType; // 启动类型

	private Collection<Integer> daysOfWeek = new ArrayList<Integer>(); // 一周中的哪几天

	private int dayOfMonth; // 一个月中的第几天

	private int repeatTimes; // 一天中运行的次数

	private int frequency; // 频率(时间/次数)

	private Date lastExecutedDate = null;

	// 任务ID与已一天中已执行次数的映射
	private int executedCount;

	/**
	 * 获取启动类型
	 * 
	 * @return 启动类型
	 */
	public int getStartupType() {
		return startupType;
	}
	
	
	public String getStartupTypeValue() {
		return getStartupName(startupType);
	}

	public static String getStartupName(int startupType) { // 启动类型名称的列表
		String name = "";
		switch (startupType) {
		case TaskConstants.STARTUP_TYPE_MANUAL:
			name = "手动";
			break;
		case TaskConstants.STARTUP_TYPE_AUTO:
			name = "自动";
			break;
		case TaskConstants.STARTUP_TYPE_BANNED:
			name = "禁止";
			break;
		default:
			break;
		}
		return name;
	}
	/**
	 * 设置启动类型
	 * 
	 * @param startupType
	 *            启动类型
	 */
	public void setStartupType(int startupType) {
		this.startupType = startupType;
	}

	/**
	 * 设置任务状态
	 * 
	 * @return 任务状态
	 */
	public int getState() {
		return state;
	}
	
	public String getStateName() {
		return getStateName(id);
	}
	
	public static String getStateName(String taskid) { // 状态名称
		Environment env = Environment.getInstance();
		try {
			boolean exists = env.getScheduler().checkExists(new JobKey(taskid, TaskConstants.TASK_GROUP));
			if (exists) {
				return TaskConstants.RUNNING;
			}
		
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		
		return TaskConstants.STOPPING;
	}

	/**
	 * 设置任务状态
	 * 
	 * @param state
	 *            任务状态
	 */
	public void setState(int state) {
		this.state = state;
	}

	/**
	 * 获取任务的描述
	 * 
	 * @return 任务的描述
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置任务的描述
	 * 
	 * @param description
	 *            任务的描述
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 获取开始时间
	 * 
	 * @return 开始时间
	 */
	public Date getRunningTime() {
		return runningTime;
	}

	/**
	 * 设置开始时间
	 * 
	 * @param runningTime
	 *            开始时间
	 */
	public void setRunningTime(Date runningTime) {
		this.runningTime = runningTime;
	}

	/**
	 * 获取标识
	 * 
	 * @return 标识
	 */
	public String getId() {
		return id;
	}

	/**
	 * 获取运行次数
	 * 
	 * @return 运行次数
	 */
	public int getRuntimes() {
		return runtimes;
	}

	/**
	 * 设置运行次数
	 * 
	 * @param runtimes
	 *            运行次数
	 */
	public void setRuntimes(int runtimes) {
		this.runtimes = runtimes;
	}

	/**
	 * 获取总运行次数
	 * @return 总运行次数
	 */
	public int getTotalRuntimes() {
		return totalRuntimes;
	}

	/**
	 * 设置总运行次数
	 * 
	 * @param totalRuntimes
	 *            总运行次数
	 */
	public void setTotalRuntimes(int totalRuntimes) {
		this.totalRuntimes = totalRuntimes;
	}

	/**
	 * 获取一个月中的第几天
	 * 
	 * @return 一个月中的第几天
	 */
	public int getDayOfMonth() {
		return dayOfMonth;
	}

	/**
	 * 设置一个月中的第几天
	 * 
	 * @param dayOfMonth
	 *            一个月中的第几天
	 */
	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	/**
	 * 获取重复类型
	 * 
	 * @hibernate.property column="PERIOD"
	 * @return 重复类型
	 */
	public int getPeriod() {
		return period;
	}
	
	public String getPeriodName() {
		return TaskConstants.getPeriodName(period);
	}

	/**
	 * 设置重复类型
	 * 
	 * @param period
	 *            重复类型
	 */
	public void setPeriod(int period) {
		this.period = period;
	}

	/**
	 * 是否可以执行此任务
	 * 
	 * @return 是否可以执行此任务(ture: 表示可执行 false:表示为可执行)
	 */
	@JsonIgnore
	public boolean isExecuteAble() {
		return isExecuteAble(new Date());
	}

	/**
	 * 是否可以执行此任务
	 * 
	 * @param sysDate
	 *            系统时间
	 * @return 如果可以执行则返回true,否则返回false
	 */
	@JsonIgnore
	public boolean isExecuteAble(Date sysDate) {
		boolean executeAble = false;

		Date nextRunningTime = getNextRunningTime(sysDate);
		// 当前时间与到期时间比较
		long timeDiff = DateUtil.getDiffTime(nextRunningTime, sysDate);

		LOG.debug("Time Difference: " + timeDiff / 1000 + "(s)");
		try {
			if (lastExecutedDate == null || DateUtil.getDistinceDay(lastExecutedDate, sysDate) >= 1) {
				this.setExecutedCount(0);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		switch (period) {
		case (TaskConstants.REPEAT_TYPE_NONE):
			// 不重复
			long dateTimeDiff = DateUtil.getDiffDateTime(getRunningTime(), sysDate);
			executeAble = isBetweenOneMinute(dateTimeDiff);
			break;

		case (TaskConstants.REPEAT_TYPE_DAILY):
			// 每天
			executeAble = isBetweenOneMinute(timeDiff);
			break;

		case (TaskConstants.REPEAT_TYPE_WEEKLY):
			// 每周
			Calendar calendar = Calendar.getInstance();
			executeAble = getDaysOfWeek().contains(Integer.valueOf(calendar.get(Calendar.DAY_OF_WEEK)))
					&& isBetweenOneMinute(timeDiff);
			break;

		case (TaskConstants.REPEAT_TYPE_MONTHLY):
			// 每月
			calendar = Calendar.getInstance();
			executeAble = getDayOfMonth() == calendar.get(Calendar.DAY_OF_MONTH) && isBetweenOneMinute(timeDiff);
			break;

		case (TaskConstants.REPEAT_TYPE_IMMEDIATE):
			// 立刻
			if (executedCount <= 0) {
				executeAble = true;
			}
			break;
		case (TaskConstants.REPEAT_TYPE_DAILY_MINUTES):
			// 每分
			executeAble = true;
			break;
		case (TaskConstants.REPEAT_TYPE_DAILY_HOURS):
			// 每时
			if (lastExecutedDate == null) {
				executeAble = true;
			} else {
				nextRunningTime = getNexitRunningTimeAtSecode(TaskConstants.ONE_HOURS);
				timeDiff = nextRunningTime.getTime() - sysDate.getTime();
				executeAble = isBetweenOneMinute2(timeDiff);
			}
			break;
		}

		if (executeAble) {
			this.addExecutedCount();
			lastExecutedDate = sysDate;
		}

		return executeAble;
	}

	/**
	 * 是否在1分钟以内(大于0小于等于60000毫秒)
	 * 
	 * @param timeDifference
	 *            时间差值
	 * @return 是否在1分钟以内, 如果是返回true, 否则返回false
	 */
	private boolean isBetweenOneMinute(long timeDifference) {
		return timeDifference > 0 && timeDifference <= 60 * 1000;
	}

	private boolean isBetweenOneMinute2(long timeDifference) {
		return timeDifference >= 0 && timeDifference <= 60 * 1000;
	}

	/**
	 * 获取下一次运行时间
	 * 
	 * @param sysDate
	 *            当前系统日期
	 * @return 日期
	 */
	private Date getNextRunningTime(Date sysDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(runningTime);

		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(sysDate);

		calendar.set(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DATE));

		return calendar.getTime();
	}
	private Date getNexitRunningTimeAtSecode(int lenght) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(lastExecutedDate.getTime() + lenght);
		return calendar.getTime();
	}

	/**
	 * 获取一周中的哪几天运行任务
	 * 
	 * @return 一周中的哪几天运行任务
	 */
	public Collection<Integer> getDaysOfWeek() {
		return daysOfWeek;
	}

	/**
	 * 设置一周中的哪几天运行任务
	 * 
	 * @param daysOfWeek
	 *            一周中的哪几天
	 */
	public void setDaysOfWeek(Collection<Integer> daysOfWeek) {
		this.daysOfWeek = daysOfWeek;
	}

	/**
	 * 获取一天中运行的次数
	 * 
	 * @hibernate.property column="REPEATTIMES"
	 * @return 运行的次数
	 */
	public int getRepeatTimes() {
		return repeatTimes;
	}

	/**
	 * 设置一天中运行的次数
	 * 
	 * @param repeatTimes
	 *            运行的次数
	 */
	public void setRepeatTimes(int repeatTimes) {
		this.repeatTimes = repeatTimes;
	}

	/**
	 * 获取频率(时间/次数)
	 * 
	 * @hibernate.property column="FREQUENCY"
	 * @return 频率(时间/次数)
	 */
	public int getFrequency() {
		return frequency;
	}

	/**
	 * 设置频率(时间/次数)
	 * 
	 * @param frequency
	 *            频率(时间/次数)
	 */
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	/**
	 * 获取任务标识与已一天中已执行次数的映射
	 * 
	 * @return 执行次数
	 */
	public int getExecutedCount() {
		return executedCount;
	}

	/**
	 * 设置任务标识与已一天中已执行次数的映射
	 * 
	 * @param executedCount
	 *            次数
	 */
	public void setExecutedCount(int executedCount) {
		this.executedCount = executedCount;
	}

	/**
	 * 克隆对象
	 * 
	 * @return Object
	 */
	public Object clone() {
		try {
			super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return ObjectUtil.clone(this);
	}

	@JsonIgnore
	public void addExecutedCount() {
		executedCount++;
	}

	@JsonBackReference
	@JsonIgnore
	public Trigger getTrigger() throws ParseException {
		TaskTriggerBuilder builder = new TaskTriggerBuilder();
		builder.addTask(this);

		return builder.build();
	}

	@JsonBackReference
	@JsonIgnore
	public JobDetail getJobDetail() throws ParseException {
		JobDataMap map = new JobDataMap();
		map.put(this.getId(), this);

		return JobBuilder.newJob(TaskJob.class).usingJobData(map).withIdentity(this.getId(), TaskConstants.TASK_GROUP)
				.build();
	}
	
	/**
	 * 执行任务
	 * TODO
	 */
	@JsonIgnore
	public abstract void execute() ;
	
	/**
	 * 是否终止
	 * TODO
	 * @return
	 * @throws Exception
	 */
	@JsonIgnore
	public abstract boolean isTerminate() throws Exception;

}
