package com.whollyframework.util.tree;

import java.util.ArrayList;
import java.util.Collection;

import com.whollyframework.util.json.JsonUtil;

public abstract class Tree<E> {
	protected Collection<Node> childNodes = new ArrayList<Node>();

	protected Collection<String> searchNodes = new ArrayList<String>();

	public Collection<? extends Node> getChildNodes() {
		return childNodes;
	}

	public void setChildNodes(Collection<Node> childNodes) {
		this.childNodes = childNodes;
	}

	public abstract void parse(Collection<E> datas);

	public abstract void search();

	public String toJSON() {
		return JsonUtil.collection2Json(childNodes);
	}

	public String toSearchJSON() {
		return JsonUtil.collection2Json(searchNodes);
	}

	public Collection<String> getSearchNodes() {
		return searchNodes;
	}

	public void setSearchNodes(Collection<String> searchNodes) {
		this.searchNodes = searchNodes;
	}
}
