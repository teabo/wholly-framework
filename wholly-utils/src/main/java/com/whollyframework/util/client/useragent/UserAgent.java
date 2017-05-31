package com.whollyframework.util.client.useragent;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;

public class UserAgent extends eu.bitwalker.useragentutils.UserAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserAgent(OperatingSystem operatingSystem, Browser browser) {
		super(operatingSystem, browser);
	}

	public UserAgent(String userAgentString) {
		super(userAgentString);
	}

}
