package edu.clarkson.gdc.simulator.impl.tact;

import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.impl.AbstractDataCenter;
import edu.clarkson.gdc.simulator.impl.tact.message.ClientRead;
import edu.clarkson.gdc.simulator.impl.tact.message.ClientResponse;
import edu.clarkson.gdc.simulator.impl.tact.message.ClientWrite;

public class IsolateServer extends AbstractDataCenter {
	@Override
	protected void processEach(Pipe source, DataMessage message,
			MessageRecorder recorder) {
		if (message instanceof ClientRead) {
			recorder.record(8l, 10l, source, new ClientResponse(message));
		}

		if (message instanceof ClientWrite) {
			recorder.record(13l, 10l, source, new ClientResponse(message));
		}
	}
}
