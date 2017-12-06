package com.whollyframework.thrift.demo;

import org.apache.thrift.TException;

import com.whollyframework.thrift.demo.HelloWorldService.Iface;

public class HelloWorldImpl implements Iface {

	@Override
	public String sayHello(String username) throws TException {
		return username + ", Hello world!";
	}

}
