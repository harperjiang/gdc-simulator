package edu.clarkson.gdc.simulator.module.network.impl;

import edu.clarkson.gdc.simulator.module.network.Region;

public class GeoRegion implements Region {

	private String id;

	private String name;

	private RandomHop hop;

	public GeoRegion() {
		super();
		hop = new RandomHop();
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

	public RandomHop getHop() {
		return hop;
	}

	public void setHop(RandomHop hop) {
		this.hop = hop;
	}

}
