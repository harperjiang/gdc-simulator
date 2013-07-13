package edu.clarkson.gdc.simulator.module.network.impl;

import edu.clarkson.gdc.simulator.module.network.Region;

public class GeoRegion implements Region {

	private String id;

	private String name;

	private Cable cable;

	public GeoRegion() {
		super();
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

	public Cable getCable() {
		return cable;
	}

	public void setCable(Cable cable) {
		this.cable = cable;
	}

}
