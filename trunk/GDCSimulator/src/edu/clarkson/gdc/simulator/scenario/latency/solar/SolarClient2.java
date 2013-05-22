package edu.clarkson.gdc.simulator.scenario.latency.solar;

import java.util.Arrays;
import java.util.Map.Entry;

import edu.clarkson.gdc.simulator.framework.Node;
import edu.clarkson.gdc.simulator.framework.NodeState;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.module.client.RandomClient;

public class SolarClient2 extends RandomClient {

	public SolarClient2() {
		waitResponse = true;
		// timeout = 2000;
	}

	private Holder[] boundary;

	int pointer = 0;

	@Override
	protected void init() {
		boundary = new Holder[getPipes().size() - 1];
		int counter = 0;
		for (Entry<Node, Pipe> entry : getPipes().entrySet()) {
			if (entry.getKey() instanceof SolarServer) {
				boundary[counter++] = new Holder(entry.getValue(), entry
						.getValue().getTimeModel().latency(null, null));
			}
		}
		Arrays.sort(boundary);
	}

	

	@Override
	protected void genRead(MessageRecorder recorder) {
		SolarClientRead scr = new SolarClientRead();
		scr.setTimeout(2000);// 20s
		for(Holder h: boundary) {
			if(h.pipe.getOpponent(this).getState() == NodeState.FREE) {
				recorder.record(h.pipe, scr);
				break;
			}
		}
	}

	@Override
	protected void genWrite(MessageRecorder recorder) {
	}

	protected static class Holder implements Comparable<Holder> {

		public Pipe pipe;

		public long noon;

		public Holder(Pipe pipe, long val) {
			this.pipe = pipe;
			this.noon = val;
		}

		@Override
		public int compareTo(Holder another) {
			return Long.valueOf(noon).compareTo(another.noon);
		}
	}
}
