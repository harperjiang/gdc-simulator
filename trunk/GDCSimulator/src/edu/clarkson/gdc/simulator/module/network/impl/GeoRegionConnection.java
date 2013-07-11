package edu.clarkson.gdc.simulator.module.network.impl;

public class GeoRegionConnection {

	private GeoRegion source;

	private GeoRegion destination;

	private RandomHop hop;

	public GeoRegionConnection() {
		hop = new RandomHop();
	}

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

	public RandomHop getHop() {
		return hop;
	}

	public void setHop(RandomHop hop) {
		this.hop = hop;
	}

}
