package edu.clarkson.gdc.simulator.framework;

import java.util.EventObject;

public class NodeMessageEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8245694877802105873L;

	private DataMessage message;

	public NodeMessageEvent(Node source, DataMessage message) {
		super(source);
		this.message = message;
	}

	public DataMessage getMessage() {
		return message;
	}
}
