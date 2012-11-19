package edu.clarkson.gdc.simulator.impl.message;

import edu.clarkson.gdc.simulator.framework.FailMessage;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 * 
 */
public class ReadKeyFail extends FailMessage {

	public ReadKeyFail(ReadKeyRequest request) {
		super(request);
	}

}
