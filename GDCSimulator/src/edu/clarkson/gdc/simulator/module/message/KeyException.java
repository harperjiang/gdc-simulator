package edu.clarkson.gdc.simulator.module.message;

import edu.clarkson.gdc.simulator.framework.Node;
import edu.clarkson.gdc.simulator.framework.NodeException;

public class KeyException extends NodeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4458917277732366666L;
	public static final int READ_NOTFOUND = 1;
	public static final int WRITE_FULL = 2;
	public static final int WRITE_NOTENOUGHCOPY = 3;

	private int error;

	public KeyException(Node node, int error) {
		super(node);
		this.error = error;
	}

	public int getError() {
		return error;
	}

}
