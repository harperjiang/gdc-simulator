package edu.clarkson.gdc.simulator.module.network.impl;

import java.util.HashMap;
import java.util.Map;

import edu.clarkson.gdc.simulator.module.network.LatencyEstimator;
import edu.clarkson.gdc.simulator.module.network.Region;

public class GeoLatencyEstimator implements LatencyEstimator {

	private Map<Object, Long> snapshot;
	
	private 
	
	public GeoLatencyEstimator() {
		super();
		snapshot = new HashMap<Object,Long>();
	}
	
	protected void takeSnapshot() {

	}

	protected long route(Region from, Region to) {
		takeSnapshot();
	}

	@Override
	public long estimate(Region from, Region to) {
		return route(from, to);
	}

}
