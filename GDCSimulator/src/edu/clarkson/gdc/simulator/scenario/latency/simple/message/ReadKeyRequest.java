package edu.clarkson.gdc.simulator.scenario.latency.simple.message;

import edu.clarkson.gdc.simulator.framework.DataMessage;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 * 
 */
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
