package edu.clarkson.gdc.event.impl;

import edu.clarkson.gdc.event.Event;

public class DefaultEvent implements Event {

	private String type;
	
	private String id;
	
	private Object content;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}
}
