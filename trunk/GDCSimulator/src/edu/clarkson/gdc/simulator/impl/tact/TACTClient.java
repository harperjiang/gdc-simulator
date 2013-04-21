package edu.clarkson.gdc.simulator.impl.tact;

import edu.clarkson.gdc.simulator.impl.RandomClient;
import edu.clarkson.gdc.simulator.impl.simple.DefaultData;
import edu.clarkson.gdc.simulator.impl.tact.message.ClientRead;
import edu.clarkson.gdc.simulator.impl.tact.message.ClientWrite;

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
