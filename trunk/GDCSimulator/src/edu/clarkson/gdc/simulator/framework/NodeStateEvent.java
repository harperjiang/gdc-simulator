package edu.clarkson.gdc.simulator.framework;

import java.util.EventObject;

public class NodeStateEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 524079835739289447L;

	private NodeState from;

	private NodeState to;

	public NodeStateEvent(Node source, NodeState from, NodeState to) {
		super(source);
		this.from = from;
		this.to = to;
	}

	public NodeState getFrom() {
		return from;
	}

	public NodeState getTo() {
		return to;
	}

	
}
