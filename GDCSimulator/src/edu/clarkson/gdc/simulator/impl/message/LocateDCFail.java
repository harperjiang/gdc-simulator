package edu.clarkson.gdc.simulator.impl.message;

import edu.clarkson.gdc.simulator.framework.FailMessage;

public class LocateDCFail extends FailMessage {

	public LocateDCFail(LocateDCRequest request) {
		super(request);
	}

}
