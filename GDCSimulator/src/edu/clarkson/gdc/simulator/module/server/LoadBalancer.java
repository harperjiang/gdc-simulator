package edu.clarkson.gdc.simulator.module.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import edu.clarkson.gdc.simulator.framework.ChainNode;
import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.Node;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.framework.ResponseMessage;

public class LoadBalancer extends ChainNode {

	private List<Pipe> cycleBuffer;

	private int pointer = 0;

	public LoadBalancer() {
		super();
		power = 100000;
		cycleBuffer = new ArrayList<Pipe>();
	}

	@Override
	protected void init() {
		for (Entry<Node, Pipe> entry : getPipes().entrySet()) {
			if (entry.getKey() instanceof AbstractDataCenter) {
				cycleBuffer.add(entry.getValue());
			}
		}
	}

	@Override
	protected void processEach(Pipe source, DataMessage message,
			MessageRecorder recorder) {
		// Do not process message, just send it back and forth, as a proxy
		if (message instanceof ResponseMessage) {
			recorder.record(getPipe(message.getSessionId()), message);
		} else {
			recorder.record(cycleBuffer.get(pointer++ % cycleBuffer.size()),
					message);
		}
	}
}
