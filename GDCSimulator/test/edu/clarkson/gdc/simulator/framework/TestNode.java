package edu.clarkson.gdc.simulator.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TestNode extends Node {

	private Map<Long, List<DataMessage>> feedback;

	public TestNode() {
		super();
		feedback = new HashMap<Long, List<DataMessage>>();
	}

	@Override
	protected List<ProcessResult> process(Map<Pipe, List<DataMessage>> events) {
		if (events == null || events.isEmpty())
			return null;

		for (Entry<Pipe, List<DataMessage>> entry : events.entrySet()) {
			if (!entry.getValue().isEmpty()
					&& entry.getValue().get(0) instanceof ResponseMessage) {
				feedback.put(getClock().getCounter(), entry.getValue());
			}
		}
		List<ProcessResult> results = new ArrayList<ProcessResult>();

		ProcessResult result = new ProcessResult();

		for (Entry<Pipe, List<DataMessage>> event : events.entrySet()) {
			for (DataMessage dm : event.getValue())
				result.add(event.getKey(), new ResponseMessage(dm) {
				});
		}
		results.add(result);
		return results;

	}

	public Map<Long, List<DataMessage>> getFeedback() {
		return feedback;
	}

}
