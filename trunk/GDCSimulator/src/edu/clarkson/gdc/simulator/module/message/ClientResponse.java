package edu.clarkson.gdc.simulator.module.message;

import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.ResponseMessage;

public class ClientResponse extends ResponseMessage {

	public ClientResponse(DataMessage request) {
		super(request);
	}

	public ClientResponse(DataMessage request, Object load) {
		super(request);
		setLoad(load);
	}

}
