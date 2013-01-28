package edu.clarkson.gdc.simulator.framework;

/**
 * If a NodeException is thrown out, the node will stop response and failed to
 * provide further service.
 * 
 * @author harper
 * @since GDCSimulator 1.0
 * @version 1.0
 * 
 * 
 */
public class NodeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1854546882794429681L;

	private Node node;

	public NodeException() {

	}
	
	public NodeException(Node node) {
		this.node = node;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

}
