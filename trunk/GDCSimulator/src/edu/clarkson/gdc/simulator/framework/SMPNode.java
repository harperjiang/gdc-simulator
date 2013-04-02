package edu.clarkson.gdc.simulator.framework;

/**
 * This node is assumed to be equipped with simultaneous multiple processor.
 * They can work independently
 * 
 * @author Harper
 * 
 */
public abstract class SMPNode extends Node {

	private int coreCount;

	public SMPNode() {
		this(1);
	}

	public SMPNode(int coreCount) {
		super();
		this.coreCount = coreCount;
	}

	@Override
	public void work() {

	}
}
