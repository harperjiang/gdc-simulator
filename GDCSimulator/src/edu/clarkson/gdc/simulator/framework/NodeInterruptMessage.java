package edu.clarkson.gdc.simulator.framework;

public class NodeInterruptMessage extends FailMessage {

	public NodeInterruptMessage(DataMessage request, NodeException exception) {
		super(request);
		setException(exception);
	}

}
