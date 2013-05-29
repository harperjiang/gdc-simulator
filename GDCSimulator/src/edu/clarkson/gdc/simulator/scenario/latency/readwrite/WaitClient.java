package edu.clarkson.gdc.simulator.scenario.latency.readwrite;

import java.util.UUID;

import edu.clarkson.gdc.simulator.module.client.RandomClient;
import edu.clarkson.gdc.simulator.module.message.KeyRead;
import edu.clarkson.gdc.simulator.module.message.KeyWrite;
import edu.clarkson.gdc.simulator.scenario.latency.simple.DefaultData;

public class WaitClient extends RandomClient {

	public WaitClient() {
		super();
		setWaitResponse(true);
	}

	protected boolean genRead(MessageRecorder recorder) {
		KeyRead read = new KeyRead();
		read.setSessionId(UUID.randomUUID().toString());
		recorder.record(getServerPipe(), read);
		return true;
	}

	protected boolean genWrite(MessageRecorder recorder) {
		KeyWrite write = new KeyWrite(new DefaultData("key"));
		write.setSessionId(UUID.randomUUID().toString());
		recorder.record(getServerPipe(), write);
		return true;
	}

}
