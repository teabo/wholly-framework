package com.whollyframework.web.login.log;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.whollyframework.util.DateUtil;
import com.whollyframework.util.StringUtil;
import com.whollyframework.util.property.DefaultProperty;
import com.whollyframework.dbservice.operatelog.model.OperateLog;

public class LoginLogThread extends Thread {
	private final static Logger LOG = LoggerFactory.getLogger(LoginLogThread.class);
	public void run() {

		LOG.info("启动登录日志写入线程！！！");
		while (!Thread.currentThread().isInterrupted()) {
			// 将登录信息写入操作日志
			try {
				if (LoginLogQueue.isEmpty()) {
					try {
						long numMillisecondsToSleep = 500; // 0.5 seconds
						Thread.sleep(numMillisecondsToSleep);
					} catch (InterruptedException e) {
					}
					continue;
				}
				LoginLog loginfo = LoginLogQueue.getQueue();

				OperateLog czrz = new OperateLog();
				if (czrz.getCreated() == null) {
					czrz.setOperateTime(toDateStr(new Date()));
					czrz.setInsertTime(new Date());
				}
				/**** 设置创建用户以及机构等 ***/
				if (StringUtil.isBlank(czrz.getAuthor())) {
					if (loginfo.getOrg() != null) {
						czrz.setOrg(loginfo.getOrg().getName());
						czrz.setOrgId(loginfo.getOrg().getId());
					}
					czrz.setAuthorId(loginfo.getUser().getId());
					czrz.setAuthor(loginfo.getUser().getName());
				}
				/**** 设置常变量 *****/
				czrz.setLastModified(DateUtil.getToday());
				czrz.setLastModifyId(loginfo.getUser().getId());
				czrz.setOperateType("0");// 操作类型

				czrz.setRegId(DefaultProperty.getProperty("whollyframework.operate.log.regId"));// 12个字符串;
																									// 应用系统/资源库标识由12位字符组成，结构如下：
				czrz.setUserId(loginfo.getUser().getLoginName());
				czrz.setOrganization("");// 待定;
				czrz.setOrganizationId("");// 待定;
				czrz.setUserName(loginfo.getUser().getName());
				czrz.setTerminalId(loginfo.getAgentip());

				czrz.setOperateResult("1");// 用户操作的结果，包括成功/失败。1:成功； 0：失败
				czrz.setErrorCode("");// 待定;
				czrz.setOperateName("用户登录");// 操作类型为0-登录时，置空；
				czrz.setOperateCondition("");// 登录
				czrz.setInsertTime(new Date());
				czrz.setCollectType("");// 待定;
				czrz.setSendid("");// 待定;

				loginfo.getOperlogService().doCreate(czrz);// 保存进操作日志
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String toDateStr(Date d) {
		String str = null;
		try {
			str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(d);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return str;
	}
}
