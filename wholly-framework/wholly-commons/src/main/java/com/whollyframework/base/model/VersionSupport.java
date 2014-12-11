package com.whollyframework.base.model;

import java.io.Serializable;

import com.whollyframework.authentications.IValueObject;

/**
 * 
 * @author Chris Xu
 *
 */
public abstract class VersionSupport implements IValueObject,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The version of value object
	 */
	protected String version="0";

	/**
	 * Get the version
	 * 
	 * @return The version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Set the version
	 * 
	 * @param version
	 *            The version to set.
	 */
	public void setVersion(String version) {
		this.version = version;
	}
}
