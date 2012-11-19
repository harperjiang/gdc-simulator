package edu.clarkson.gdc.simulator.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.event.EventListenerList;

import org.apache.commons.lang.Validate;

import edu.clarkson.gdc.simulator.framework.DataMessage.PathNode;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 */
public abstract class Node extends Component {

	public static class ProcessGroup {

		private ProcessResult success;

		private ProcessResult failed;

		public ProcessGroup(ProcessResult s, ProcessResult f) {
			this.success = s;
			this.failed = f;
		}

		public ProcessResult getSuccess() {
			return success;
		}

		public void setSuccess(ProcessResult success) {
			this.success = success;
		}

		public ProcessResult getFailed() {
			return failed;
		}

		public void setFailed(ProcessResult failed) {
			this.failed = failed;
		}

	}

	public static class ProcessResult {

		private long timestamp;

		private Map<Pipe, List<DataMessage>> messages;

		public ProcessResult() {
			super();
			messages = new HashMap<Pipe, List<DataMessage>>();
		}

		public long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}

		public Map<Pipe, List<DataMessage>> getMessages() {
			return messages;
		}

		public void add(Pipe pipe, DataMessage msg) {
			if (!messages.containsKey(pipe)) {
				List<DataMessage> msgs = new ArrayList<DataMessage>();
				messages.put(pipe, msgs);
			}
			messages.get(pipe).add(msg);
		}
	}

	private Map<Node, Pipe> pipes;

	protected Queue<ProcessResult> buffer;

	public Node() {
		super();
		pipes = new HashMap<Node, Pipe>();
		buffer = new ConcurrentLinkedQueue<ProcessResult>();
		listenerList = new EventListenerList();
	}

	@Override
	public void process() {
		Map<Pipe, List<DataMessage>> events = new HashMap<Pipe, List<DataMessage>>();
		// Collect Input & Response
		for (Pipe inputPipe : pipes.values()) {
			events.put(inputPipe, inputPipe.get(this));
		}
		// Process Event
		ProcessGroup resultGroup = process(events);
		if (null != resultGroup) {
			ProcessResult result = resultGroup.getSuccess();
			if (null != result) {
				result.setTimestamp(getLatency() + getClock().getCounter());
				for (List<DataMessage> eventList : result.getMessages()
						.values())
					for (DataMessage event : eventList) {
						event.access(new PathNode(this, result.getTimestamp()));
					}
				// Put Events to send buffer
				buffer.offer(result);
			}
			ProcessResult failed = resultGroup.getFailed();
			if (null != failed) {
				// Failed Result returns immediately
				failed.setTimestamp(getClock().getCounter());
				for (List<DataMessage> fail : failed.getMessages().values())
					for (DataMessage failms : fail) {
						failms.access(new PathNode(this, failed.getTimestamp()));
					}
				// Put Events to send buffer
				buffer.offer(failed);
			}
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
						.getMessages().entrySet()) {
					for (DataMessage event : entry.getValue())
						entry.getKey().put(this, event);
				}
				break;
			}
		}
	}

	protected abstract ProcessGroup process(Map<Pipe, List<DataMessage>> events);

	public void addPipe(Pipe pipe) {
		Validate.isTrue(this.equals(pipe.getSource())
				|| this.equals(pipe.getDestination()));
		Node other = this.equals(pipe.getSource()) ? pipe.getDestination()
				: pipe.getSource();
		pipes.put(other, pipe);
	}

	protected Pipe getPipe(Node other) {
		return pipes.get(other);
	}

	protected void reportSuccess(ResponseMessage response) {
		NodeResponseEvent event = new NodeResponseEvent(this, response);
		for (NodeListener listener : listenerList
				.getListeners(NodeListener.class))
			listener.successReceived(event);
	}

	protected void reportFailure(FailMessage fail) {
		NodeResponseEvent event = new NodeResponseEvent(this, fail);
		for (NodeListener listener : listenerList
				.getListeners(NodeListener.class))
			listener.failureReceived(event);
	}

	private EventListenerList listenerList;

	public void addListener(NodeListener listener) {
		listenerList.add(NodeListener.class, listener);
	}

	public void removeListener(NodeListener listener) {
		listenerList.remove(NodeListener.class, listener);
	}
}
