package edu.clarkson.gdc.simulator.module.server.twopc;

import edu.clarkson.gdc.simulator.framework.ResponseMessage;

public class VoteResponse extends ResponseMessage {

	private boolean accept;
	
	public VoteResponse(VoteMessage request,boolean a) {
		super(request);
		this.accept = a;
	}

	public boolean isAccept() {
		return accept;
	}
}
