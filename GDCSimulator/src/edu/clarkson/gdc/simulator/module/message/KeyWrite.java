package edu.clarkson.gdc.simulator.module.message;

import edu.clarkson.gdc.simulator.Data;
import edu.clarkson.gdc.simulator.framework.DataMessage;

public class KeyWrite extends DataMessage {

	public KeyWrite(Data data) {
		super();
		setLoad(data);
	}

	public Data getData() {
		return getLoad();
	}
}
