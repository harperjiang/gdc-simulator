package edu.clarkson.gdc.simulator.scenario.latency.simple;

import edu.clarkson.gdc.simulator.Data;

public class DefaultData implements Data {

	private String key;

	public DefaultData(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

}
