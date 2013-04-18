package edu.clarkson.gdc.simulator.impl.latency.twopc;

import edu.clarkson.gdc.simulator.framework.DataMessage;

public class FinalizeMessage extends DataMessage {

	private boolean commit;

	public FinalizeMessage(String sid, boolean c) {
		super();
		setSessionId(sid);
		this.commit = c;
	}

	public boolean isCommit() {
		return commit;
	}

}
