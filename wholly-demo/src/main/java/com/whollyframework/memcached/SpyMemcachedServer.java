package com.whollyframework.memcached;

public class SpyMemcachedServer {

	private String ip;
	private int port;
	private String username, password;

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}

	public void setPort(int port) {
		if (port < 0 || port > 65535) {
			throw new IllegalArgumentException(
					"Port number must be between 0 to 65535");
		}
		this.port = port;
	}

	public int getPort() {
		return port;
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

	public String toString() {
		return new StringBuilder().append(getIp()).append(":")
				.append(getPort()).append("@").append(getUsername())
				.append("/").append(getPassword()).toString();
	}
}
