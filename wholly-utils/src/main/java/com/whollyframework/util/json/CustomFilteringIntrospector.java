package com.whollyframework.util.json;

import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

public class CustomFilteringIntrospector extends JacksonAnnotationIntrospector {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object findFilterId(AnnotatedClass ac) {
		// First, let's consider @JsonFilter by calling superclass
		Object id = super._findFilterId(ac);
		// but if not found, use our own heuristic; say, just use class name as filter id, if there's "Filter" in name:
		if (id == null) {
			String name = ac.getName();
			if (name.indexOf("Filter") >= 0) {
				id = name;
			}
		}
		return id;
	}
}
