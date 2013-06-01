package edu.clarkson.gdc.simulator.scenario.workload.sharepopular;

import edu.clarkson.gdc.simulator.module.client.RandomClient;

public class ShareClient extends RandomClient {

	public ShareClient() {
		super();
		setReadRatio(0.05);
	}
	
	@Override
	protected boolean genRead(MessageRecorder recorder) {
		return false;
	}

	@Override
	protected boolean genWrite(MessageRecorder recorder) {
		// TODO Auto-generated method stub
		return false;
	}

}
