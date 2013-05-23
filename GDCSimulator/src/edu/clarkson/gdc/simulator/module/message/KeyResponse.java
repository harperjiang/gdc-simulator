package edu.clarkson.gdc.simulator.module.message;

import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.ResponseMessage;

public class KeyResponse extends ResponseMessage {

	public KeyResponse(DataMessage request) {
		super(request);
	}

	public KeyResponse(DataMessage request, Object load) {
		super(request);
		setLoad(load);
	}

}
