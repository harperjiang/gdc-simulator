package edu.clarkson.gdc.simulator.framework;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 */
public abstract class ResponseMessage extends DataMessage {

	private DataMessage request;

	public DataMessage getRequest() {
		return request;
	}

	public ResponseMessage(DataMessage request) {
		this.request = request;
	}
}
