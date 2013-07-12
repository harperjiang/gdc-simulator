package edu.clarkson.gdc.simulator.module.network.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.clarkson.gdc.simulator.module.network.LatencyEstimator;
import edu.clarkson.gdc.simulator.module.network.Region;

public class GeoLatencyEstimator implements LatencyEstimator {

	private Map<Object, Long> snapshot;

	private AdjMatrix adjMatrix;
	
	private List<GeoRegion> locations;
	
	private List<RegionConnection> connections;

	public GeoLatencyEstimator() {
		super();
		snapshot = new HashMap<Object, Long>();
		adjMatrix = new AdjMatrix();
	}

	@Override
	public long estimate(Region from, Region to) {
		return route(from, to);
	}

	protected void takeSnapshot() {
		adjMatrix = new AdjMatrix();
	}

	protected long route(Region from, Region to) {
		takeSnapshot();
	}

	protected void addConnection(GeoRegion from, GeoRegion to, long min,
			long max) {
		
	}

}
