package edu.clarkson.gdc.simulator.scenario.workload.sharepopular;

import edu.clarkson.gdc.simulator.module.client.RandomClient;

public class ReadClient extends RandomClient {

	public ReadClient() {
		super();
		setReadRatio(0.99);
	}

	@Override
	protected boolean genRead(MessageRecorder recorder) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean genWrite(MessageRecorder recorder) {
		// TODO Auto-generated method stub
		return false;
	}

}
