package edu.clarkson.gdc.simulator.impl.latency;

import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.ResponseMessage;

public class ClientResponse extends ResponseMessage {

	private boolean success;

	public ClientResponse(DataMessage request,boolean s) {
		super(request);
		success = s;
	}

	public boolean isSuccess() {
		return success;
	}
}
