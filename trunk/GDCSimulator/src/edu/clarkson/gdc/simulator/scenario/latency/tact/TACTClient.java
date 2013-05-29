package edu.clarkson.gdc.simulator.scenario.latency.tact;

import edu.clarkson.gdc.simulator.module.client.RandomClient;
import edu.clarkson.gdc.simulator.module.message.KeyRead;
import edu.clarkson.gdc.simulator.module.message.KeyWrite;
import edu.clarkson.gdc.simulator.scenario.latency.simple.DefaultData;

public class TACTClient extends RandomClient {

	public TACTClient() {
		super();
		waitResponse = true;
		readRatio = 0.5f;
	}

	@Override
	protected boolean genRead(MessageRecorder recorder) {
		recorder.record(getServerPipe(), new KeyRead());
		return true;
	}

	@Override
	protected boolean genWrite(MessageRecorder recorder) {
		recorder.record(getServerPipe(), new KeyWrite(new DefaultData("key")));
		return true;
	}

}
