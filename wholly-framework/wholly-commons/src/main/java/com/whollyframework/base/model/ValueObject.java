package com.whollyframework.base.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 对于基于jpa和hibernate管理的实体类，统一主键生成策略
 * 
 * @author chris xu
 * 
 */
@MappedSuperclass
public class ValueObject extends VersionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6508505596605103257L;

	/**
	 * 对象ID
	 */
	protected String id;

	/**
	 * 上级ID
	 */
	protected String parentId;

	/**
	 * 起草人ID
	 */
	protected String authorId;

	/**
	 * 起草人名称
	 */
	protected String author;

	/**
	 * 起草部门ID
	 */
	protected String orgId;

	/**
	 * 起草部门
	 */
	protected String org;
	/**
	 * 起草时间
	 */
	protected Date created;

	/**
	 * 最后修改时间
	 */
	protected Date lastModified;

	/**
	 * 最后修改人ID
	 */
	protected String lastModifyId;

	/**
	 * 排序ID
	 */
	protected int sortId;

	private int istemp;

	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ID")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getLastModifyId() {
		return lastModifyId;
	}

	public void setLastModifyId(String lastModifyId) {
		this.lastModifyId = lastModifyId;
	}

	public int getSortId() {
		return sortId;
	}

	public void setSortId(int sortId) {
		this.sortId = sortId;
	}

	public int getIstemp() {
		return istemp;
	}

	public void setIstemp(int istemp) {
		this.istemp = istemp;
	}
}
