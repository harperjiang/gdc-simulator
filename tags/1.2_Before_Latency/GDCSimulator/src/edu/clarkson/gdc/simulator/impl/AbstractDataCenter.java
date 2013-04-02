package edu.clarkson.gdc.simulator.impl;

import edu.clarkson.gdc.simulator.DataCenter;
import edu.clarkson.gdc.simulator.framework.Node;

public abstract class AbstractDataCenter extends Node implements DataCenter {

	private Object location;

	public Object getLocation() {
		return location;
	}

	public void setLocation(Object location) {
		this.location = location;
	}

}
