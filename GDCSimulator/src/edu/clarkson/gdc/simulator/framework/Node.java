package edu.clarkson.gdc.simulator.framework;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;

import edu.clarkson.gdc.simulator.ExceptionStrategy;
import edu.clarkson.gdc.simulator.framework.DataMessage.PathNode;
import edu.clarkson.gdc.simulator.framework.NodeState.NodeStateMachine;
import edu.clarkson.gdc.simulator.framework.utils.EventListenerDelegate;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 */
public abstract class Node extends Component implements NodeStateListener {

	private NodeStateMachine stateMachine;

	private Map<Node, Pipe> pipes;

	protected Queue<ProcessResult> buffer;

	private ExceptionStrategy exceptionStrategy;

	private EventListenerDelegate listenerDelegate;

	public Node() {
		super();
		pipes = new HashMap<Node, Pipe>();
		buffer = new PriorityQueue<ProcessResult>();

		listenerDelegate = new EventListenerDelegate();

		stateMachine = new NodeStateMachine();
	}

	protected Map<Pipe, List<DataMessage>> collectInput() {
		Map<Pipe, List<DataMessage>> events = new HashMap<Pipe, List<DataMessage>>();
		for (Pipe inputPipe : pipes.values()) {
			List<DataMessage> input = inputPipe.get(this);
			if (!input.isEmpty())
				events.put(inputPipe, input);
		}
		return events;
	}

	@Override
	public void process() {
		try {
			// Update State Machine Info
			stateMachine.tick();

			NodeException exception = null;

			if (getState() == NodeState.DOWN) {
				stateMachine
						.exception(new NodeStateException(this, getState()));
			}
			// Check whether exception strategy will work
			if (null != getExceptionStrategy()) {
				exception = getExceptionStrategy().getException(
						getClock().getCounter());
				if (null != exception) {
					exception.setNode(this);
					stateMachine.exception(exception);
				} else {
					// No Exception, switch back to free status
					if (getState() == NodeState.EXCEPTION) {
						// Switch back to FREE status
						stateMachine.free();
					}
				}
			}
			switch (getState()) {
			case BUSY:
				return;
			case DOWN:
			case EXCEPTION: {
				Map<Pipe, List<DataMessage>> events = collectInput();
				// Send a response indicating node has exception
				ProcessResult result = new ProcessResult();
				result.setException(true);
				if (!events.isEmpty()) {
					for (Entry<Pipe, List<DataMessage>> entry : events
							.entrySet()) {
						for (DataMessage msg : entry.getValue()) {
							FailMessage fail = new NodeFailMessage(msg,
									stateMachine.getException());
							result.add(entry.getKey(), fail);
						}
					}
					result.setTimestamp(getClock().getCounter());
					buffer.offer(result);
				}
			}
				break;
			case FREE: {
				Map<Pipe, List<DataMessage>> events = collectInput();
				// Calculate Latency
				long latency = getLatency(events);
				// Process Event
				List<ProcessResult> results = process(events);
				if (!CollectionUtils.isEmpty(results)) {
					for (ProcessResult result : results) {
						if (null != result && null != result.getMessages()
								&& !result.getMessages().isEmpty()) {
							result.setTimestamp(getClock().getCounter(),
									latency);
							for (List<DataMessage> eventList : result
									.getMessages().values())
								for (DataMessage event : eventList) {
									event.access(new PathNode(this, result
											.getTimestamp()));
								}
							// Put Events to send buffer
							buffer.offer(result);
						}
					}
					stateMachine.busy(latency);
				}
			}
				break;
			default:
				throw new IllegalStateException(getState().toString());
			}
		} catch (Exception e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void send() {
		while (true) {
			// Get Event that is ready
			ProcessResult timeoutResult = buffer.peek();
			if (null == timeoutResult)
				break;
			if (NodeState.EXCEPTION == getState()
					|| NodeState.DOWN == getState()) { // Exception case,
														// immediate send out
														// all pending message
				timeoutResult = buffer.poll();
				NodeException e = (getState() == NodeState.EXCEPTION) ? stateMachine
						.getException() : new NodeStateException(this,
						getState());
				for (Entry<Pipe, List<DataMessage>> entry : timeoutResult
						.getMessages().entrySet()) {
					for (DataMessage event : entry.getValue()) {
						if (event instanceof NodeFailMessage) {
							entry.getKey().put(this, event);
						} else if (event instanceof ResponseMessage) {
							ResponseMessage rm = (ResponseMessage) event;
							FailMessage exm = new NodeFailMessage(
									rm.getRequest(), e);
							entry.getKey().put(this, exm);
						} else {
							// In exception state node cannot send
							// message, intercept all request message
						}
					}
				}
			} else { // Normal case, wait till message is ready to be sent
				if (timeoutResult.getTimestamp() < getClock().getCounter()) {
					timeoutResult = buffer.poll();
					// Distribute
					for (Entry<Pipe, List<DataMessage>> entry : timeoutResult
							.getMessages().entrySet()) {
						for (DataMessage event : entry.getValue())
							entry.getKey().put(this, event);
					}
				} else {
					break;
				}
			}
		}
	}

	protected abstract List<ProcessResult> process(
			Map<Pipe, List<DataMessage>> events);

	public NodeState getState() {
		return stateMachine.getState();
	}

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

	protected Map<Node, Pipe> getPipes() {
		return pipes;
	}

	protected void fireSuccess(ResponseMessage response) {
		NodeResponseEvent event = new NodeResponseEvent(this, response);
		for (NodeMessageListener listener : listenerDelegate
				.getListeners(NodeMessageListener.class))
			listener.successReceived(event);
	}

	protected void fireFailure(FailMessage fail) {
		NodeResponseEvent event = new NodeResponseEvent(this, fail);
		for (NodeMessageListener listener : listenerDelegate
				.getListeners(NodeMessageListener.class))
			listener.failureReceived(event);
	}

	protected void fireStateChange(NodeStateEvent event) {
		for (NodeStateListener listener : listenerDelegate
				.getListeners(NodeStateListener.class))
			listener.stateChanged(event);
	}

	@Override
	public void stateChanged(NodeStateEvent event) {
		fireStateChange(new NodeStateEvent(this, event.getFrom(), event.getTo()));
	}

	public <EL extends EventListener> void addListener(EL listener) {
		listenerDelegate.addListener(listener);
	}

	public <EL extends EventListener> void removeListener(EL listener) {
		listenerDelegate.removeListener(listener);
	}

	public <EL extends EventListener> EL[] getListeners(Class<EL> listenerClass) {
		return listenerDelegate.getListeners(listenerClass);
	}

	public ExceptionStrategy getExceptionStrategy() {
		return exceptionStrategy;
	}

	public void setExceptionStrategy(ExceptionStrategy exceptionStrategy) {
		this.exceptionStrategy = exceptionStrategy;
	}

	public static class ProcessResult implements Comparable<ProcessResult> {

		private long timestamp;

		private BigDecimal factor = BigDecimal.ONE;

		private Map<Pipe, List<DataMessage>> messages;

		private boolean exception;

		public ProcessResult() {
			super();
			messages = new HashMap<Pipe, List<DataMessage>>();
		}

		public ProcessResult(BigDecimal factor) {
			this();
			this.factor = factor;
		}

		public boolean isException() {
			return exception;
		}

		public void setException(boolean exception) {
			this.exception = exception;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}

		public void setTimestamp(long clock, long latency) {
			setTimestamp(clock
					+ new BigDecimal(latency).multiply(factor).longValue());
		}

		public BigDecimal getFactor() {
			return factor;
		}

		public void setFactor(BigDecimal factor) {
			this.factor = factor;
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

		@Override
		public int compareTo(ProcessResult o) {
			return Long.valueOf(this.getTimestamp())
					.compareTo(o.getTimestamp());
		}
	}
}
