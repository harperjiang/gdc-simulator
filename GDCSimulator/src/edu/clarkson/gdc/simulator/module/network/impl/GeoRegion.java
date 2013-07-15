package edu.clarkson.gdc.simulator.module.network.impl;

import edu.clarkson.gdc.simulator.module.network.Region;

public class GeoRegion implements Region {

	private String id;

	private Cable cable;

	public GeoRegion() {
		super();
	}

	public GeoRegion(String id, Cable cable) {
		this();
		setId(id);
		setCable(cable);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Cable getCable() {
		return cable;
	}

	public void setCable(Cable cable) {
		this.cable = cable;
	}

}
