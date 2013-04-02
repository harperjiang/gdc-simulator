package edu.clarkson.gdc.simulator.impl.latency;

import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.impl.AbstractDataCenter;

public class LatencyServer extends AbstractDataCenter {
	@Override
	protected void processEach(Pipe source, DataMessage message,
			MessageRecorder recorder) {
		if (message instanceof LatencyRead) {
			recorder.record(100l, source, new LatencyResponse(message));
		}

		if (message instanceof LatencyWrite) {
			recorder.record(500l, source, new LatencyResponse(message));
		}

	}
}
