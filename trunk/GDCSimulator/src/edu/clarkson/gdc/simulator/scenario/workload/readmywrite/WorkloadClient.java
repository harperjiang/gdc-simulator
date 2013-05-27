package edu.clarkson.gdc.simulator.scenario.workload.readmywrite;

import edu.clarkson.gdc.simulator.module.client.RandomClient;
import edu.clarkson.gdc.simulator.module.message.KeyRead;
import edu.clarkson.gdc.simulator.module.message.KeyWrite;
import edu.clarkson.gdc.simulator.scenario.latency.simple.DefaultData;

public class WorkloadClient extends RandomClient {

	public WorkloadClient(int index, double readratio, int interval) {
		super(readratio, interval, -1, true);
		this.setId(String.valueOf(index));
	}

	@Override
	protected void genRead(MessageRecorder recorder) {
		KeyRead keyRead = new KeyRead();
		keyRead.setKey("dummy");
		recorder.record(getServerPipe(), keyRead);
		// FIXME Implement the logic
	}

	@Override
	protected void genWrite(MessageRecorder recorder) {
		KeyWrite keyWrite = new KeyWrite(new DefaultData("dummy"));
		recorder.record(getServerPipe(), keyWrite);
		// FIXME Implement the logic
	}

}
