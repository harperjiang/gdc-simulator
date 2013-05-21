package edu.clarkson.gdc.simulator.scenario.simple.message;

import edu.clarkson.gdc.simulator.framework.ResponseMessage;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 * 
 */
public class ReadKeyResponse extends ResponseMessage {

	public ReadKeyResponse(ReadKeyRequest request) {
		super(request);
	}

}
