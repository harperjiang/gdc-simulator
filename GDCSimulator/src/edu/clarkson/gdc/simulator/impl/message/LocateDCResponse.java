package edu.clarkson.gdc.simulator.impl.message;

import edu.clarkson.gdc.simulator.framework.ResponseMessage;

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
