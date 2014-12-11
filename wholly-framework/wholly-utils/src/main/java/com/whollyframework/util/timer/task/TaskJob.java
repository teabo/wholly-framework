package com.whollyframework.util.timer.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 定时任务作业
 * 
 * @author znicholas
 * @date 2012-12-29
 * 
 */
public class TaskJob implements Job {

	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			String id = context.getJobDetail().getKey().getName();
			Task task = (Task) context.getJobDetail().getJobDataMap().get(id);
			if (task != null) {
				task.execute();
				task.setTotalRuntimes(task.getTotalRuntimes() + 1);
			}
		} catch (Exception e) {
			throw new JobExecutionException(e);
		}
	}
}
