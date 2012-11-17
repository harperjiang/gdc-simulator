package edu.clarkson.gdc.simulator.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang.Validate;

import edu.clarkson.gdc.simulator.framework.DataMessage.PathNode;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 */
public abstract class Node extends Component {

	public static class ProcessResult {
		private long timestamp;

		private Map<Pipe, List<DataMessage>> events;

		public long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}

		public Map<Pipe, List<DataMessage>> getEvents() {
			return events;
		}

		public void setEvents(Map<Pipe, List<DataMessage>> events) {
			this.events = events;
		}

	}

	private List<Pipe> pipes;

	private Queue<ProcessResult> buffer;

	public Node() {
		super();
		pipes = new ArrayList<Pipe>();
		buffer = new ConcurrentLinkedQueue<ProcessResult>();
	}

	public List<Pipe> getPipes() {
		return pipes;
	}

	@Override
	public void process() {
		Map<Pipe, List<DataMessage>> events = new HashMap<Pipe, List<DataMessage>>();
		// Collect Input & Response
		for (Pipe inputPipe : pipes) {
			events.put(inputPipe, inputPipe.get(this));
		}
		// Process Event
		ProcessResult result = process(events);
		if (null != result) {
			result.setTimestamp(getLatency() + getClock().getCounter());
			for (List<DataMessage> eventList : result.getEvents().values())
				for (DataMessage event : eventList) {
					event.access(new PathNode(this, result.getTimestamp()));
				}
			// Put Events to send buffer
			buffer.offer(result);
		}

	}

	public void send() {
		// Get Event that is ready
		while (true) {
			ProcessResult timeoutResult = buffer.peek();
			if (null == timeoutResult)
				break;
			if (timeoutResult.getTimestamp() <= getClock().getCounter()) {
				timeoutResult = buffer.poll();
				// Distribute
				for (Entry<Pipe, List<DataMessage>> entry : timeoutResult
						.getEvents().entrySet()) {
					for (DataMessage event : entry.getValue())
						entry.getKey().put(this, event);
				}
				break;
			}
		}

	}

	protected abstract ProcessResult process(Map<Pipe, List<DataMessage>> events);

	public void addPipe(Pipe pipe) {
		Validate.isTrue(this.equals(pipe.getSource())
				|| this.equals(pipe.getDestination()));
		getPipes().add(pipe);
	}

}
