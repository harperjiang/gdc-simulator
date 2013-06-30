package edu.clarkson.gdc.event.impl;

import org.apache.commons.lang.StringUtils;

import edu.clarkson.gdc.event.Event;
import edu.clarkson.gdc.event.EventSelector;

public class TypeSelector implements EventSelector {

	private String type;

	private Class<?> clazz;

	@Override
	public boolean accept(Event event) {
		if (StringUtils.isNotEmpty(type) && !type.equals(event.getType()))
			return false;
		if (clazz != null && !clazz.isInstance(event.getContent()))
			return false;
		return true;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

}
