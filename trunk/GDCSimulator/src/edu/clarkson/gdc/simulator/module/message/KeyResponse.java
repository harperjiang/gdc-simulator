package edu.clarkson.gdc.simulator.module.message;

import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.ResponseMessage;

public class KeyResponse extends ResponseMessage {

	private int response;

	public KeyResponse(DataMessage request) {
		this(request, null);
	}

	public KeyResponse(DataMessage request, int response, Object load) {
		super(request);
		setLoad(load);
		this.response = response;
	}

	public KeyResponse(DataMessage request, Object load) {
		this(request, 0, load);
	}

	public int getResponse() {
		return response;
	}

	public void setResponse(int response) {
		this.response = response;
	}

}
