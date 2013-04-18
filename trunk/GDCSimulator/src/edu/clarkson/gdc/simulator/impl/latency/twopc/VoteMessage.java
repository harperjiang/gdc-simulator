package edu.clarkson.gdc.simulator.impl.latency.twopc;

import edu.clarkson.gdc.simulator.Data;
import edu.clarkson.gdc.simulator.framework.DataMessage;

public class VoteMessage extends DataMessage {

	private Data towrite;

	public VoteMessage(String sessionId, Data towrite) {
		super();
		this.setSessionId(sessionId);
		this.towrite = towrite;
	}

	public Data getTowrite() {
		return towrite;
	}
}
