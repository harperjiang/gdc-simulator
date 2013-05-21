package edu.clarkson.gdc.simulator.scenario.latency;

import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.scenario.AbstractDataCenter;

public class IsolateServer extends AbstractDataCenter {
	@Override
	protected void processEach(Pipe source, DataMessage message,
			MessageRecorder recorder) {
		if (message instanceof ClientRead) {
			recorder.record(30l, 100l, source,
					new ClientResponse(message, true));
		}

		if (message instanceof ClientWrite) {
			recorder.record(40l, 120l, source, new ClientResponse(message,
					true));
		}
	}
}
