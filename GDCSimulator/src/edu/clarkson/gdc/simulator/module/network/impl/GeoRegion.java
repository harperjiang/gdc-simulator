package edu.clarkson.gdc.simulator.module.network.impl;

import edu.clarkson.gdc.simulator.module.network.Region;

public class GeoRegion implements Region {

	private String id;

	private String name;

	private LatencyCable hop;

	public GeoRegion() {
		super();
		hop = new LatencyCable();
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

	public LatencyCable getHop() {
		return hop;
	}

	public void setHop(LatencyCable hop) {
		this.hop = hop;
	}

}
