package edu.clarkson.gdc.simulator.framework;

public abstract class FailMessage extends ResponseMessage {

	public FailMessage(DataMessage request) {
		super(request);
	}
}
