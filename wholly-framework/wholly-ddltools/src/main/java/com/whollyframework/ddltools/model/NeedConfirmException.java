package com.whollyframework.ddltools.model;

import java.util.Collection;
import java.util.Iterator;

/**
 * 
 * @author chris
 * @since 2011-12-20
 * 
 */
public class NeedConfirmException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Collection<Confirm> confirms;

	public NeedConfirmException(Collection<Confirm> confirms) {
		this.confirms = confirms;
	}

	public Collection<Confirm> getConfirms() {
		return confirms;
	}

	public void setConfirms(Collection<Confirm> confirms) {
		this.confirms = confirms;
	}

	public String getMessage() {
		StringBuffer strbuf = new StringBuffer();

		for (Iterator<Confirm> iter = confirms.iterator(); iter.hasNext();) {
			Confirm confirm = (Confirm) iter.next();
			strbuf.append(confirm.getMsgKeyName() + "\n");
		}
		return strbuf.toString();

	}
}
