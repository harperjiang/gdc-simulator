package edu.clarkson.gdc.simulator.module.message;

import java.util.UUID;

import edu.clarkson.gdc.simulator.framework.DataMessage;

public class ClientRead extends DataMessage {
	public ClientRead() {
		super();
		setSessionId(UUID.randomUUID().toString());
	}

	public String getKey() {
		return getLoad();
	}

	public void setKey(String key) {
		setLoad(key);
	}
}
