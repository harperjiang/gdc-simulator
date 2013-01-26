package edu.clarkson.gdc.simulator.framework;

public class NodeDownException extends NodeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6775225974867831775L;

	private Node server;

	private NodeState state;

	public NodeDownException(Node server, NodeState state) {
		this.server = server;
		this.state = state;
	}

	public Node getServer() {
		return server;
	}

	public NodeState getState() {
		return state;
	}

}
