package edu.clarkson.gdc.simulator.module.message;

import edu.clarkson.gdc.simulator.Data;
import edu.clarkson.gdc.simulator.framework.DataMessage;

public class ClientWrite extends DataMessage {

	public ClientWrite(Data data) {
		super();
		setLoad(data);
	}

	public Data getData() {
		return getLoad();
	}
}
