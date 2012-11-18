package edu.clarkson.gdc.simulator.framework;

import java.util.EventObject;

public class NodeResponseEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3140900344176840221L;

	public NodeResponseEvent(DataMessage source) {
		super(source);
	}
}
