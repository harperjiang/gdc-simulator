package edu.clarkson.gdc.simulator.scenario.latency;

import java.util.UUID;

import edu.clarkson.gdc.simulator.scenario.RandomClient;
import edu.clarkson.gdc.simulator.scenario.simple.DefaultData;

public class WaitClient extends RandomClient {

	public WaitClient() {
		super();
		setWaitResponse(true);
	}

	protected void genRead(MessageRecorder recorder) {
		ClientRead read = new ClientRead();
		read.setSessionId(UUID.randomUUID().toString());
		recorder.record(getServerPipe(), read);
	}

	protected void genWrite(MessageRecorder recorder) {
		ClientWrite write = new ClientWrite(new DefaultData("key"));
		write.setSessionId(UUID.randomUUID().toString());
		recorder.record(getServerPipe(), write);
	}

}
