package com.whollyframework.dbservice.userbindip.model;

import com.whollyframework.base.model.ValueObject;
import com.whollyframework.dbservice.userbindip.util.IP2LongUtils;
/**
 * 用户管理模块--IP绑定
 * @author WangWenGuang
 * @2015-1-13
 */
public class BindIp extends ValueObject implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String startIp; // 起始IP
	private String userId; // 绑定用户
	private String endIp; // 结束IP

	public String getStartIp() {
		return startIp;
	}

	public void setStartIp(String startIp) {
		this.startIp = startIp;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEndIp() {
		return endIp;
	}

	public void setEndIp(String endIp) {
		this.endIp = endIp;
	}
    /**
     * 判断指定IP是否在IP范围内
     * @param resourceIp
     * @return
     */
	public boolean isInnserIp(String resourceIp){
		return IP2LongUtils.isInnerIp(startIp, endIp, resourceIp);
	}
	
}