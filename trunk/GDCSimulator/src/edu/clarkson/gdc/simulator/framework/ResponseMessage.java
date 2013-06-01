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
		if (request instanceof ResponseMessage)
			return ((ResponseMessage) request).getRequest();
		return request;
	}

	public ResponseMessage(DataMessage request) {
		super();
		setRequest(request);
	}

	public void setRequest(DataMessage request) {
		this.request = request;
		if (request != null && !StringUtils.isEmpty(request.getSessionId())) {
			setSessionId(request.getSessionId());
		}
	}

}
