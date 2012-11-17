package edu.clarkson.gdc.simulator.impl.message;

import edu.clarkson.gdc.simulator.framework.DataMessage;

public class ReadKeyMessage extends DataMessage {

	private String key;

	public ReadKeyMessage(String key) {
		super();
		this.key = key;
	}

	public String getKey() {
		return key;
	}

}
