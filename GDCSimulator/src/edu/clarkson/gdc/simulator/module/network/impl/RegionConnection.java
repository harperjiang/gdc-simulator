package edu.clarkson.gdc.simulator.module.network.impl;

public class RegionConnection {

	private GeoRegion source;

	private GeoRegion destination;

	private Cable cable;

	public GeoRegion getSource() {
		return source;
	}

	public void setSource(GeoRegion source) {
		this.source = source;
	}

	public GeoRegion getDestination() {
		return destination;
	}

	public void setDestination(GeoRegion destination) {
		this.destination = destination;
	}

	public Cable getCable() {
		return cable;
	}

	public void setCable(Cable cable) {
		this.cable = cable;
	}

}
