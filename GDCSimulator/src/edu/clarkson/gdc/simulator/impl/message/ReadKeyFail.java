package edu.clarkson.gdc.simulator.impl.message;

import edu.clarkson.gdc.simulator.framework.FailMessage;

public class ReadKeyFail extends FailMessage {

	public ReadKeyFail(ReadKeyRequest request) {
		super(request);
	}
	

}
