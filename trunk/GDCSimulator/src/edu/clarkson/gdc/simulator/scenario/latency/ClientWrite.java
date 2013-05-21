package edu.clarkson.gdc.simulator.scenario.latency;

import edu.clarkson.gdc.simulator.Data;
import edu.clarkson.gdc.simulator.framework.DataMessage;

public class ClientWrite extends DataMessage {

	private Data data;
	
	public ClientWrite(Data data) {
		this.data = data;
	}
	
	public Data getData() {
		return data;
	}
}
