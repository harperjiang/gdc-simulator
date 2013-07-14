package edu.clarkson.gdc.simulator.module.network.impl;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class AdjMatrix {

	private Map<Object, Integer> indexMap;

	private Map<Object, Integer> latencyMap;

	private long[][] matrix;

	public AdjMatrix() {
		indexMap = new HashMap<Object, Integer>();
		latencyMap = new HashMap<Object, Integer>();
	}

	public void addVertex(GeoRegion region, int latency) {
		indexMap.put(region, indexMap.size());
		latencyMap.put(region, latency);
	}

	public void addEdge(GeoRegion source, GeoRegion destination, int latency) {
		String edgeIndex = MessageFormat.format("{0}:{1}",
				indexMap.get(source), indexMap.get(destination));
		latencyMap.put(edgeIndex, latency);
	}

	public long latency(GeoRegion from, GeoRegion to) {
		// TODO Not implemented
		makeMatrix();
	}

	protected void makeMatrix() {
		if (null != matrix) {
			return;
		}
		matrix = new long[indexMap.size()][indexMap.size()];
		for (Entry<Object, Integer> entry : indexMap.entrySet()) {
			matrix[entry.getValue()][entry.getValue()] = latencyMap.get(entry
					.getKey());
			latencyMap.remove(entry.getKey());
		}
		for()
	}
}
