package edu.clarkson.gdc.simulator.impl.latency;

import java.util.UUID;

import edu.clarkson.gdc.simulator.impl.RandomClient;
import edu.clarkson.gdc.simulator.impl.simple.DefaultData;

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
