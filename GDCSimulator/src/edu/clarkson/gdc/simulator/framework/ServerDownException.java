package edu.clarkson.gdc.simulator.framework;

public class ServerDownException extends NodeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6775225974867831775L;

	private Node server;

	public ServerDownException(Node server) {
		this.server = server;
	}

	public Node getServer() {
		return server;
	}

}
