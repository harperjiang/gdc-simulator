package edu.clarkson.gdc.simulator.module.message;

import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.ResponseMessage;

public class ClientResponse extends ResponseMessage {

	private boolean success;

	public ClientResponse(DataMessage request) {
		this(request, true);
	}

	public ClientResponse(DataMessage request, boolean s) {
		super(request);
		success = s;
	}

	public boolean isSuccess() {
		return success;
	}
}
