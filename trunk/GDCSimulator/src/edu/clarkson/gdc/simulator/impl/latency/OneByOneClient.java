package edu.clarkson.gdc.simulator.impl.latency;

import edu.clarkson.gdc.simulator.impl.RandomClient;
import edu.clarkson.gdc.simulator.impl.simple.DefaultData;

public class OneByOneClient extends RandomClient {

	public OneByOneClient() {
		super();
		setWaitResponse(true);
	}

	protected void genRead(MessageRecorder recorder) {
		recorder.record(getServerPipe(), new LatencyRead());
	}

	protected void genWrite(MessageRecorder recorder) {
		recorder.record(getServerPipe(), new LatencyWrite(
				new DefaultData("key")));
	}

}
