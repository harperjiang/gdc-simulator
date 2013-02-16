package edu.clarkson.gdc.dashboard.domain.entity;

import java.util.HashMap;
import java.util.Map;

public class Node {

	private String id;

	private String name;

	private String description;

	private Map<String, String> attributes;

	public Node() {
		super();
		attributes = new HashMap<String, String>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

}
