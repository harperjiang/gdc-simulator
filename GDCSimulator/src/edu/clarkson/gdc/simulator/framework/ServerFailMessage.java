package edu.clarkson.gdc.simulator.framework;

public class ServerFailMessage extends FailMessage {

	public ServerFailMessage(DataMessage request, NodeException exception) {
		super(request);
		setException(exception);
	}

}
