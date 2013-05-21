package edu.clarkson.gdc.simulator.scenario.tact.message;

import edu.clarkson.gdc.simulator.Data;
import edu.clarkson.gdc.simulator.framework.DataMessage;

public class ClientWrite extends DataMessage {

	public ClientWrite(Data data) {
		super();
		this.setLoad(data);
	}
	
	public Data getData() {
		return getLoad();
	}
}
