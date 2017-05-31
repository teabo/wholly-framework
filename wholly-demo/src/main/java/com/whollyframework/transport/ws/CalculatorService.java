package com.whollyframework.transport.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalculatorService {

	private static final Logger log = LoggerFactory.getLogger(CalculatorService.class);
	
	public double add(double param1, double param2){
		log.debug(param1 + " + " + param2 + " = " + (param1 + param2));
		return param1 + param2;
	}
	
	public double sub(double param1, double param2){
		log.debug(param1 + " - " + param2 + " = " + (param1 - param2));
		return param1 - param2;
	}
}
