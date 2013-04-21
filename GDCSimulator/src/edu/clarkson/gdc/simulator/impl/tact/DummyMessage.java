package edu.clarkson.gdc.simulator.impl.tact;

import edu.clarkson.gdc.simulator.framework.DataMessage;

public class DummyMessage extends DataMessage {

	private String content;
	
	public DummyMessage(String c) {
		this.content = c;
	}
	
	public String toString() {
		return content;
	}
}
