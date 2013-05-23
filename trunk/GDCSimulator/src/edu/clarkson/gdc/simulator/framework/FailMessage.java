package edu.clarkson.gdc.simulator.framework;

public abstract class FailMessage extends ResponseMessage {

	private NodeException exception;

	public FailMessage(DataMessage request) {
		super(request);
	}

	public FailMessage(DataMessage request, NodeException e) {
		this(request);
		exception = e;
	}

	public NodeException getException() {
		return exception;
	}

	public void setException(NodeException exception) {
		this.exception = exception;
	}

}
