package com.whollyframework.support;

public class JsonResult {
	private boolean success;
	private String message;
	public JsonResult() {
		super();
		// TODO Auto-generated constructor stub
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public static JsonResult buildSuccessResult(String message){
		JsonResult result = new JsonResult();
		result.setMessage(message);
		result.setSuccess(true);
		return result;
	}
	
	public static JsonResult buildErrorResult(String message){
		JsonResult result = new JsonResult();
		result.setMessage(message);
		result.setSuccess(false);
		return result;
	}
}
