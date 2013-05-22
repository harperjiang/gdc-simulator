package edu.clarkson.gdc.simulator.module.server;

import java.util.HashMap;
import java.util.Map;

import edu.clarkson.gdc.simulator.DataCenter;
import edu.clarkson.gdc.simulator.framework.Node;
import edu.clarkson.gdc.simulator.framework.storage.Storage;

public abstract class AbstractDataCenter extends Node implements DataCenter {

	private Object location;

	protected Map<String, Long> cpuCosts;

	public AbstractDataCenter() {
		super();
		cpuCosts = new HashMap<String, Long>();
	}

	@SuppressWarnings("unchecked")
	public <T> T getLocation() {
		return (T) location;
	}

	public void setLocation(Object location) {
		this.location = location;
	}

	private Storage storage;

	public Storage getStorage() {
		return storage;
	}

	public void setStorage(Storage storage) {
		this.storage = storage;
	}

	protected long getCpuCost(String key) {
		return cpuCosts.get(key);
	}

	protected void setCpuCost(String key, long cost) {
		cpuCosts.put(key, cost);
	}
}
