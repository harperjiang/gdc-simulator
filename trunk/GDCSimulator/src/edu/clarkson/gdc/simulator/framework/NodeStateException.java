package edu.clarkson.gdc.simulator.framework;

public class NodeStateException extends NodeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3684809249981188719L;

	private NodeState state;

	public NodeState getState() {
		return state;
	}

	public NodeStateException(NodeState state) {
		super();
		this.state = state;
	}
	
	public NodeStateException(Node node, NodeState state) {
		super(node);
		this.state = state;
	}
}
