package edu.clarkson.gdc.simulator.impl.latency;

import edu.clarkson.gdc.simulator.Data;
import edu.clarkson.gdc.simulator.framework.DataMessage;

public class LatencyWrite extends DataMessage {

	private Data data;
	
	public LatencyWrite(Data data) {
		this.data = data;
	}
	
	public Data getData() {
		return data;
	}
}
