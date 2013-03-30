package edu.clarkson.gdc.simulator.framework;

public class NodeFailMessage extends FailMessage {

	public NodeFailMessage(DataMessage request, NodeException exception) {
		super(request);
		setException(exception);
	}

}
