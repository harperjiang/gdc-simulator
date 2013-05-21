package edu.clarkson.gdc.simulator.module.client;

import edu.clarkson.gdc.simulator.Client;
import edu.clarkson.gdc.simulator.framework.Node;

public abstract class AbstractClient extends Node implements Client {

	private Object location;

	@SuppressWarnings("unchecked")
	public <T> T getLocation() {
		return (T) location;
	}

	public void setLocation(Object location) {
		this.location = location;
	}

}
