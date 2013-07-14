package edu.clarkson.gdc.simulator.module.network.impl;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
		makeMatrix();
		int start = indexMap.get(from);
		int stop = indexMap.get(to);

		// Use Dijkstra's algorithm
		return dijkstra(start, stop);
	}

	protected void makeMatrix() {
		if (null != matrix) {
			return;
		}
		matrix = new long[indexMap.size()][indexMap.size()];
		for (int i = 0; i < indexMap.size(); i++) {
			for (int j = 0; j < indexMap.size(); j++) {
				matrix[i][j] = Long.MAX_VALUE;
			}
		}
		for (Entry<Object, Integer> entry : indexMap.entrySet()) {
			matrix[entry.getValue()][entry.getValue()] = latencyMap.get(entry
					.getKey());
			latencyMap.remove(entry.getKey());
		}
		for (Entry<Object, Integer> entry : latencyMap.entrySet()) {
			RegionConnection con = (RegionConnection) entry.getKey();
			matrix[indexMap.get(con.getSource())][indexMap.get(con
					.getDestination())] = entry.getValue();
			matrix[indexMap.get(con.getDestination())][indexMap.get(con
					.getSource())] = entry.getValue();
		}
	}

	protected long dijkstra(int start, int stop) {
		boolean[] visited = new boolean[indexMap.size()];
		long[] dist = new long[indexMap.size()];
		for (int i = 0; i < visited.length; i++) {
			visited[i] = false;
			dist[i] = Long.MAX_VALUE;
		}
		visited[start] = true;
		dist[start] = matrix[start][start];

		Set<Integer> remain = new HashSet<Integer>();
		for (int i = 0; i < visited.length; i++)
			remain.add(i);

		while (remain.size() > 0) {
			int next = -1;
			long min = Long.MAX_VALUE;
			for (Integer value : remain) {
				if (dist[value] < min) {
					min = dist[value];
					next = value;
				}
			}
			if (min == Long.MAX_VALUE) {
				// All remaining are untouchable
				break;
			}
			remain.remove(next);

			for (int i = 0; i < visited.length; i++) {
				if (Long.MAX_VALUE != matrix[next][i]
						&& (dist[next] + matrix[next][i] + matrix[i][i] < dist[i])) {
					dist[i] = dist[next] + matrix[next][i] + matrix[i][i];
				}
			}

			if (!visited[stop])
				break;
		}

		return dist[stop];
	}
}
