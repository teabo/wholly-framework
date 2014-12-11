package com.whollyframework.util.tree;


public abstract  class Node {

	private String id = ""; // 节点ID
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public abstract String toXml();

}
