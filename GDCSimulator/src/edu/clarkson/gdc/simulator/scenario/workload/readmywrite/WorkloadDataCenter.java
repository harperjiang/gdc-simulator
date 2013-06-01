package edu.clarkson.gdc.simulator.scenario.workload.readmywrite;

import edu.clarkson.gdc.simulator.Data;
import edu.clarkson.gdc.simulator.common.Pair;
import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.NodeFailMessage;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.framework.storage.StorageException;
import edu.clarkson.gdc.simulator.module.message.KeyRead;
import edu.clarkson.gdc.simulator.module.message.KeyResponse;
import edu.clarkson.gdc.simulator.module.message.KeyWrite;
import edu.clarkson.gdc.simulator.module.server.AbstractDataCenter;

public class WorkloadDataCenter extends AbstractDataCenter {

	private long requestProcessTime;

	public WorkloadDataCenter(long reqprctime) {
		super();
		requestProcessTime = reqprctime;
	}

	@Override
	protected void processEach(Pipe source, DataMessage message,
			MessageRecorder recorder) {
		try {
			if (message instanceof KeyRead) {
				KeyRead read = (KeyRead) message;
				Pair<Long, Data> result = getStorage().read(read.getKey());
				recorder.record(requestProcessTime, result.getA(), source,
						new KeyResponse(message, result.getB()));
			}
			if (message instanceof KeyWrite) {
				KeyWrite write = (KeyWrite) message;
				long time = getStorage().write(write.getData());
				recorder.record(requestProcessTime, time, source,
						new KeyResponse(message));
			}
		} catch (StorageException e) {
			recorder.record(requestProcessTime, source, new NodeFailMessage(
					message, e));
		}
	}
}
