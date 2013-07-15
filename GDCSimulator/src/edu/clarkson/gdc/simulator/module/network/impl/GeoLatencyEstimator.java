package edu.clarkson.gdc.simulator.module.network.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;

import edu.clarkson.gdc.simulator.module.network.LatencyEstimator;
import edu.clarkson.gdc.simulator.module.network.Region;

public class GeoLatencyEstimator implements LatencyEstimator {

	private Set<GeoRegion> regions;

	private Map<String, GeoRegion> regionIndex;

	private List<RegionConnection> connections;

	public GeoLatencyEstimator() {
		super();

		regions = new HashSet<GeoRegion>();
		regionIndex = new HashMap<String, GeoRegion>();
		connections = new ArrayList<RegionConnection>();
	}

	@Override
	public long estimate(Region from, Region to) {
		Validate.isTrue(regions.contains(from));
		Validate.isTrue(regions.contains(to));
		return route((GeoRegion) from, (GeoRegion) to);
	}

	protected AdjMatrix takeSnapshot() {
		AdjMatrix matrix = new AdjMatrix();

		for (GeoRegion region : regions) {
			matrix.addVertex(region, region.getCable().getLatency());
		}
		for (RegionConnection connection : connections) {
			matrix.addEdge(connection.getSource(), connection.getDestination(),
					connection.getCable().getLatency());
		}

		return matrix;
	}

	protected long route(GeoRegion from, GeoRegion to) {
		AdjMatrix matrix = takeSnapshot();
		return matrix.latency(from, to);
	}

	protected void addRegion(GeoRegion region) {
		regions.add(region);
		regionIndex.put(region.getId(), region);
	}

	protected void addConnection(RegionConnection connection) {
		regions.add(connection.getSource());
		regions.add(connection.getDestination());
		connections.add(connection);
	}

	@Override
	public Region query(String id) {
		return regionIndex.get(id);
	}

}
