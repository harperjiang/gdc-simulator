package edu.clarkson.gdc.simulator.scenario.tact;

import edu.clarkson.gdc.simulator.module.client.RandomClient;
import edu.clarkson.gdc.simulator.module.message.ClientRead;
import edu.clarkson.gdc.simulator.module.message.ClientWrite;
import edu.clarkson.gdc.simulator.scenario.simple.DefaultData;

public class TACTClient extends RandomClient {

	public TACTClient() {
		super();
		waitResponse = true;
		readRatio = 0.5f;
	}

	@Override
	protected void genRead(MessageRecorder recorder) {
		recorder.record(getServerPipe(), new ClientRead());
	}

	@Override
	protected void genWrite(MessageRecorder recorder) {
		recorder.record(getServerPipe(),
				new ClientWrite(new DefaultData("key")));
	}

}
