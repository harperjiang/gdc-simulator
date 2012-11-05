package edu.clarkson.gdc.simulator.event;

import java.util.EventObject;

public class SimulatorEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2559210387323411816L;

	private long timestamp;

	private SimulatorEventType type;

	public SimulatorEvent(Object source) {
		super(source);
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public SimulatorEventType getType() {
		return type;
	}

	public void setType(SimulatorEventType type) {
		this.type = type;
	}

}
