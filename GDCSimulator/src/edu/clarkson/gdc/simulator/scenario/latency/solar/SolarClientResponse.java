package edu.clarkson.gdc.simulator.scenario.latency.solar;

import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.ResponseMessage;

public class SolarClientResponse extends ResponseMessage {

	public SolarClientResponse(DataMessage request) {
		super(request);
	}

}
