package edu.clarkson.gdc.simulator.scenario.tact;

import edu.clarkson.gdc.simulator.scenario.RandomClient;
import edu.clarkson.gdc.simulator.scenario.simple.DefaultData;
import edu.clarkson.gdc.simulator.scenario.tact.message.ClientRead;
import edu.clarkson.gdc.simulator.scenario.tact.message.ClientWrite;

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
