package edu.clarkson.gdc.simulator.scenario.simple.message;

import edu.clarkson.gdc.simulator.framework.ResponseMessage;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 * 
 */
public class LocateDCResponse extends ResponseMessage {

	private String dataCenterId;

	public LocateDCResponse(LocateDCRequest ldcm, String dataCenterId) {
		super(ldcm);
		this.dataCenterId = dataCenterId;
	}

	public String getDataCenterId() {
		return dataCenterId;
	}

}
