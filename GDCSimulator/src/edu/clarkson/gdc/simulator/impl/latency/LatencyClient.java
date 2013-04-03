package edu.clarkson.gdc.simulator.impl.latency;

import java.util.Random;

import edu.clarkson.gdc.simulator.DataCenter;
import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.impl.AbstractClient;
import edu.clarkson.gdc.simulator.impl.simple.DefaultData;

public class LatencyClient extends AbstractClient {

	private double readRatio = 1;

	private long interval = 100;

	private Random random = new Random(System.currentTimeMillis() * hashCode());

	@Override
	protected void processNew(MessageRecorder recorder) {
		if (0 != random.nextInt((int) interval))
			return;
		if (random.nextDouble() < readRatio) {
			recorder.record(getServerPipe(), new LatencyRead());
		} else {
			recorder.record(getServerPipe(), new LatencyWrite(new DefaultData(
					"key")));
		}
	}

	@Override
	protected void processEach(Pipe source, DataMessage message,
			MessageRecorder recorder) {

	}

	@Override
	protected void processSummary(MessageRecorder recorder) {

	}

	private Pipe serverPipe;

	protected Pipe getServerPipe() {
		if (null != serverPipe)
			return serverPipe;
		if (getPipes().size() != 1)
			throw new IllegalArgumentException(
					"Client should have only one connection");
		serverPipe = getPipes().values().iterator().next();
		if (!(serverPipe.getOpponent(this) instanceof DataCenter)) {
			throw new IllegalArgumentException(
					"Client can only connect to DataCenter");
		}
		return serverPipe;
	}
}
