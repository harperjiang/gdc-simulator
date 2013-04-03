package edu.clarkson.gdc.simulator.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestNode extends Node {

	private Map<Long, List<DataMessage>> feedback;

	public TestNode() {
		super();
		feedback = new HashMap<Long, List<DataMessage>>();
	}

	@Override
	protected void processEach(Pipe source, DataMessage message,
			MessageRecorder recorder) {

		if (message instanceof ResponseMessage) {
			if (!feedback.containsKey(getClock().getCounter())) {
				feedback.put(getClock().getCounter(),
						new ArrayList<DataMessage>());
			}
			feedback.get(getClock().getCounter()).add(message);
		} else {
			recorder.record(2l, source, new ResponseMessage(message) {
			});
		}
	}

	public Map<Long, List<DataMessage>> getFeedback() {
		return feedback;
	}

}
