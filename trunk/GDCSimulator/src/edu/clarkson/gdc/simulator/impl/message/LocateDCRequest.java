package edu.clarkson.gdc.simulator.impl.message;

import edu.clarkson.gdc.simulator.framework.DataMessage;

public class LocateDCRequest extends DataMessage {

	private String key;

	public LocateDCRequest(String key) {
		super();
		this.key = key;
	}

	public String getKey() {
		return key;
	}

}
