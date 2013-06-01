package edu.clarkson.gdc.simulator.scenario.workload.sharepopular;

import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.module.message.KeyResponse;
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
		recorder.record(requestProcessTime, source, new KeyResponse(message));
	}

}
