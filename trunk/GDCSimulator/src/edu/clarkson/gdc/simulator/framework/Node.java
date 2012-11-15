package edu.clarkson.gdc.simulator.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import edu.clarkson.gdc.simulator.framework.DataEvent.PathNode;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 */
public abstract class Node extends Component {

	public static class ProcessResult {
		private long timestamp;

		private Map<Pipe, List<DataEvent>> events;

		public long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}

		public Map<Pipe, List<DataEvent>> getEvents() {
			return events;
		}

		public void setEvents(Map<Pipe, List<DataEvent>> events) {
			this.events = events;
		}

	}

	private List<Pipe> inputs;

	private List<Pipe> outputs;

	public Node() {
		super();
		inputs = new ArrayList<Pipe>();
		outputs = new ArrayList<Pipe>();

		buffer = new ConcurrentLinkedQueue<ProcessResult>();
	}

	public List<Pipe> getInputs() {
		return inputs;
	}

	public List<Pipe> getOutputs() {
		return outputs;
	}

	private Queue<ProcessResult> buffer;

	@Override
	public void process() {
		Map<Pipe, List<DataEvent>> events = new HashMap<Pipe, List<DataEvent>>();
		for (Pipe inputPipe : inputs) {
			events.put(inputPipe, inputPipe.get());
		}
		ProcessResult result = process(events);
		result.setTimestamp(getLatency() + getClock().getCounter());
		for (List<DataEvent> eventList : result.getEvents().values())
			for (DataEvent event : eventList) {
				event.access(new PathNode(this, result.getTimestamp()));
			}
		// Put Events to buffer
		buffer.offer(result);

	}

	public void send() {
		// Get Event that is ready
		while (true) {
			ProcessResult timeoutResult = buffer.peek();
			if (timeoutResult.getTimestamp() <= getClock().getCounter()) {
				timeoutResult = buffer.poll();
				// Distribute
				for (Entry<Pipe, List<DataEvent>> entry : timeoutResult
						.getEvents().entrySet()) {
					for (DataEvent event : entry.getValue())
						entry.getKey().put(event);
				}
				break;
			}
		}

	}

	protected abstract ProcessResult process(Map<Pipe, List<DataEvent>> events);

}
