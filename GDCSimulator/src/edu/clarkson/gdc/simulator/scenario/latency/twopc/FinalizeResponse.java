package edu.clarkson.gdc.simulator.scenario.latency.twopc;

import edu.clarkson.gdc.simulator.framework.ResponseMessage;

public class FinalizeResponse extends ResponseMessage {

	public FinalizeResponse(FinalizeMessage request) {
		super(request);
	}

}
