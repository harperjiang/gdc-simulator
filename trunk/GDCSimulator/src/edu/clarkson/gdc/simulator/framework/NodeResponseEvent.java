package edu.clarkson.gdc.simulator.framework;

import java.util.EventObject;

public class NodeResponseEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3140900344176840221L;

	private DataMessage message;

	public NodeResponseEvent(Node source, DataMessage msg) {
		super(source);
		this.message = msg;
	}

	public DataMessage getMessage() {
		return message;
	}

}
