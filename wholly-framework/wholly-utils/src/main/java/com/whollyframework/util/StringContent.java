package com.whollyframework.util;

public class StringContent {

	private String content;
	private String fromcharset;
	private String tocharset;

	public StringContent(String content, String fromcharset, String tocharset) {
		this.content = content;
		this.fromcharset = fromcharset;
		this.tocharset = tocharset;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFromcharset() {
		return fromcharset;
	}

	public void setFromcharset(String fromcharset) {
		this.fromcharset = fromcharset;
	}

	public String getTocharset() {
		return tocharset;
	}

	public void setTocharset(String tocharset) {
		this.tocharset = tocharset;
	}
	
	public byte[] getBytes() {
		try {
			String context = new String(content.getBytes(), fromcharset);
			System.out.println("Content String --> " + context);
			return context.getBytes(tocharset);
		} catch (Exception e) {
			return null;
		}
	}

	public String toString() {
		try {
			String context = new String(content.getBytes(), fromcharset);
			System.out.println("Content String --> " + context);
			return new String(context.getBytes(tocharset), tocharset);
		} catch (Exception e) {
			return null;
		}
	}
}
