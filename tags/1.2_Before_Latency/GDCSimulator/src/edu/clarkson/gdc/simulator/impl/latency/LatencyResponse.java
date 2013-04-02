package edu.clarkson.gdc.simulator.impl.latency;

import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.ResponseMessage;

public class LatencyResponse extends ResponseMessage {

	public LatencyResponse(DataMessage request) {
		super(request);
	}

}
