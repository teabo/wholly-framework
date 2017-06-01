package com.whollyframework.web.user.model;

import com.whollyframework.base.model.ValueObject;

/**
 * @hibernate.class table="T_USER" batch-size="10" lazy="false"
 */
public class UserVO extends ValueObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8254654111907418034L;

	/**
	 * 等级
	 */
	private int level; //

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * 备注
	 */
	private String remarks;

}
