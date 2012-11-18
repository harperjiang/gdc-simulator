package edu.clarkson.gdc.simulator.impl.message;

import edu.clarkson.gdc.simulator.framework.DataMessage;

public class ReadKeyRequest extends DataMessage {

	private String key;

	public ReadKeyRequest(String key) {
		super();
		this.key = key;
	}

	public String getKey() {
		return key;
	}

}
