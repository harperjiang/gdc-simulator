package edu.clarkson.gdc.simulator.scenario.tact.message;

import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.ResponseMessage;

public class ClientResponse extends ResponseMessage {

	public ClientResponse(DataMessage request) {
		super(request);
	}

}
