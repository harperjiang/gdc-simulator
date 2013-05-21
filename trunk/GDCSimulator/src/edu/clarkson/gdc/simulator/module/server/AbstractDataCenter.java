package edu.clarkson.gdc.simulator.module.server;

import edu.clarkson.gdc.simulator.DataCenter;
import edu.clarkson.gdc.simulator.framework.Node;

public abstract class AbstractDataCenter extends Node implements DataCenter {

	private Object location;

	@SuppressWarnings("unchecked")
	public <T> T getLocation() {
		return (T) location;
	}

	public void setLocation(Object location) {
		this.location = location;
	}

}
