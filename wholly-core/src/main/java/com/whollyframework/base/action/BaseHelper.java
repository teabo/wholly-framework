package com.whollyframework.base.action;

import com.whollyframework.base.service.IDesignService;

public abstract class BaseHelper<E> {

	private IDesignService<E, String> service;

	public BaseHelper(IDesignService<E, String> service) {
		this.service = service;
	}

	public IDesignService<E, String> getService() {
		return service;
	}
}
