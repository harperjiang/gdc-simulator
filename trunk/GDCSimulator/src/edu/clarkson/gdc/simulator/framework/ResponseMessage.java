package edu.clarkson.gdc.simulator.framework;

import org.apache.commons.lang.StringUtils;

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
		super();
		setRequest(request);
	}

	public void setRequest(DataMessage request) {
		this.request = request;
		if (!StringUtils.isEmpty(request.getSessionId())) {
			setSessionId(request.getSessionId());
		}
	}

}
