package com.whollyframework.util.timer.task.listener;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerKey;
import org.quartz.TriggerListener;
import org.quartz.listeners.TriggerListenerSupport;

import com.whollyframework.constans.TaskConstants;
import com.whollyframework.util.timer.task.Task;

public class TaskTriggerListener extends TriggerListenerSupport implements TriggerListener {
	private final static Logger LOG = Logger.getLogger(TaskTriggerListener.class);

	public String getName() {
		return "TaskTriggerListener";
	}

	/**
	 * 已触发完成
	 */
	public void triggerFired(Trigger trigger, JobExecutionContext context) {
		super.triggerFired(trigger, context);

		// 调用更新task执行次数
		JobDetail detail = context.getJobDetail();
		String id = detail.getKey().getName();
		Task task = (Task) detail.getJobDataMap().get(id);

		try {
			if (task != null && task.isTerminate()) { // 中断任务
				TriggerKey key = new TriggerKey(task.getId(), TaskConstants.TASK_GROUP);
				context.getScheduler().unscheduleJob(key);

				LOG.info("TaskTriggerListener interrupt job " + detail.getKey() + ".");
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}

	public void triggerComplete(Trigger trigger, JobExecutionContext context,
			CompletedExecutionInstruction triggerInstructionCode) {
		super.triggerComplete(trigger, context, triggerInstructionCode);
	}
}
