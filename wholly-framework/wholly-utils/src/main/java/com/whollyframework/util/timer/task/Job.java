package com.whollyframework.util.timer.task;

public abstract class Job implements Runnable {

	// 用户名
	private String username;

	// 密码
	private String password;

	// 访问地址
	private String url;
	
	private String[] args;
	
	public Job(){
		
	}
	
	public Job(String... args){
		this.args = args;
	}

	public String[] getArgs() {
		return args;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
