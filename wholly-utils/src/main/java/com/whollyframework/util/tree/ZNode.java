package com.whollyframework.util.tree;

public class ZNode extends Node {

	private String name; // 节点名称
	private String icon; //图标
	private boolean isParent; // open, closed
	private String viewid;// 视图编号
	private String curr_node;// 当前节点
	private String super_node_fieldName;// 上级节点表单字段名
	private String formid;// 表单编号
	private String nodeValue;// 节点值
	private String valuesMap;// 值列表
	private boolean checked;// 是否选中
	private String link; // 页签中打开的链接
	private String pId; // 父节点ID

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}

	public String getViewid() {
		return viewid;
	}

	public void setViewid(String viewid) {
		this.viewid = viewid;
	}

	public String getCurr_node() {
		return curr_node;
	}

	public void setCurr_node(String currNode) {
		curr_node = currNode;
	}

	public String getSuper_node_fieldName() {
		return super_node_fieldName;
	}

	public void setSuper_node_fieldName(String superNodeFieldName) {
		super_node_fieldName = superNodeFieldName;
	}

	public String getFormid() {
		return formid;
	}

	public void setFormid(String formid) {
		this.formid = formid;
	}

	public String getNodeValue() {
		return nodeValue;
	}

	public void setNodeValue(String nodeValue) {
		this.nodeValue = nodeValue;
	}

	public String getValuesMap() {
		return valuesMap;
	}

	public void setValuesMap(String valuesMap) {
		this.valuesMap = valuesMap;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String toXml() {
		return "";
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}
