package com.whollyframework.util;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
	private String id;
	private String text;
	private String state;
	private boolean checked;
	private String href;
	private List<TreeNode> children;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> childrens) {
		this.children = childrens;
	}
	
	public void appendChild(TreeNode node){
		if(children==null){
			children = new ArrayList<TreeNode>();
		}
		children.add(node);
	}

}
