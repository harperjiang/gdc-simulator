package edu.clarkson.gdc.simulator.framework;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.clarkson.gdc.simulator.framework.DataMessage.PathNode;
import edu.clarkson.gdc.simulator.framework.NodeState.NodeStateMachine;
import edu.clarkson.gdc.simulator.framework.session.Session;
import edu.clarkson.gdc.simulator.framework.session.SessionManager;
import edu.clarkson.gdc.simulator.framework.utils.EventListenerDelegate;

/**
 * 
 * 
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 * @see Session
 * @see SessionManager
 */
public abstract class Node extends Component {

	protected NodeStateMachine stateMachine;

	protected Pipe selfPipe;

	protected Map<Node, Pipe> pipes;

	protected ExceptionStrategy exceptionStrategy;

	protected EventListenerDelegate listenerDelegate;

	protected List<ProcessResult> newBuffer;

	protected List<ProcessResult> toioBuffer;

	protected List<ProcessResult> cpuBuffer;

	protected List<ProcessResult> ioBuffer;

	protected Logger logger;

	private Random random = new Random(System.currentTimeMillis());

	protected int power = 1;

	protected int capacity = -1;

	protected int slowPart = 0;

	public Node() {
		super();
		pipes = new HashMap<Node, Pipe>();
		listenerDelegate = new EventListenerDelegate();
		stateMachine = new NodeStateMachine(this);

		selfPipe = new SelfPipe(this);

		logger = LoggerFactory.getLogger(getClass());

		newBuffer = new ArrayList<ProcessResult>();
		cpuBuffer = new ArrayList<ProcessResult>();
		ioBuffer = new ArrayList<ProcessResult>();
		toioBuffer = new ArrayList<ProcessResult>();
	}

	protected Pipe getSelfPipe() {
		return selfPipe;
	}

	protected Map<Pipe, List<DataMessage>> collectInput() {
		Map<Pipe, List<DataMessage>> events = new HashMap<Pipe, List<DataMessage>>();
		for (Pipe inputPipe : pipes.values()) {
			List<DataMessage> input = inputPipe.get(this);
			if (!input.isEmpty()) {
				events.put(inputPipe, input);
				for (DataMessage message : input) {
					message.access(this, getClock().getCounter());
					fireMessageReceived(message);
				}
			}
		}
		return events;
	}

	@Override
	public void work() {
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

			// Process data according to state
			switch (getState()) {
			case BUSY:
				return;
			case DOWN:
			case EXCEPTION: {
				Map<Pipe, List<DataMessage>> events = collectInput();
				// Send a response indicating node has exception
				if (!events.isEmpty()) {
					for (Entry<Pipe, List<DataMessage>> entry : events
							.entrySet()) {
						for (DataMessage msg : entry.getValue()) {
							FailMessage fail = new NodeFailMessage(msg,
									stateMachine.getException());
							fail.access(this, getClock().getCounter());
							entry.getKey().put(this, fail);
						}
					}
				}
				// In exception status, messages that waiting for process
				// will be emptied, error returned
				for (ProcessResult pr : cpuBuffer) {
					FailMessage fail = new NodeInterruptMessage(pr.message,
							stateMachine.getException());
					fail.access(this, getClock().getCounter());
					pr.getPipe().put(this, fail);
				}
				for (ProcessResult pr : ioBuffer) {
					FailMessage fail = new NodeInterruptMessage(pr.message,
							stateMachine.getException());
					fail.access(this, getClock().getCounter());
					pr.getPipe().put(this, fail);
				}
				cpuBuffer.clear();
				ioBuffer.clear();
			}
				break;
			case FREE: {
				Map<Pipe, List<DataMessage>> events = collectInput();
				// callback
				beforeProcess(events);
				// Process Event
				MessageRecorder recorder = new MessageRecorder();
				// Generate new message
				processNew(recorder);
				for (Entry<Pipe, List<DataMessage>> entry : events.entrySet()) {
					for (DataMessage message : entry.getValue()) {
						processEach(entry.getKey(), message, recorder);
					}
				}
				// Generate summary
				processSummary(recorder);
				List<ProcessResult> results = recorder.summarize();
				// callback
				afterProcess(events, results);

				// Put Events to send buffer
				newBuffer.addAll(results);

				// Decrease cpu count until power is used up
				/*
				 * With more requests to process, higher possibility to slow
				 * down. This is simulated by adding a random delay
				 */
				int remainPower = getPower();
				if (slowPart != 0) {
					double limit = 2 * Math.atan(cpuBuffer.size()) / Math.PI;
					if (random.nextDouble() < limit)
						remainPower = Math.max(0, remainPower - slowPart);
				}
				for (int i = 0; i < remainPower; i++) {
					if (cpuBuffer.isEmpty()) {
						break;
					}

					int index = random.nextInt(cpuBuffer.size());
					ProcessResult pr = cpuBuffer.get(index);
					pr.cpuDecrease();
					if (pr.cpuReady()) {
						pr = cpuBuffer.remove(index);
						if (pr.ready())
							ioBuffer.add(pr);
						else
							toioBuffer.add(pr);
					}
				}
				cpuBuffer.addAll(newBuffer);
				newBuffer.clear();

				// Exceed capacity, will not accept further message
				if (capacity != -1 && cpuBuffer.size() > capacity)
					stateMachine.busy(1);
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
		// Send IO ready message, decrease unready
		for (int i = 0; i < ioBuffer.size(); i++) {
			ProcessResult pr = ioBuffer.get(i);
			if (pr.ready()) {
				pr.getMessage().access(
						new PathNode(this, getClock().getCounter()));
				pr.getPipe().put(this, pr.getMessage());
				ioBuffer.remove(i);
				i--;
			} else {
				pr.ioDecrease();
			}
		}
		// Add process result that finishes cpu phase to io buffer
		ioBuffer.addAll(toioBuffer);
		toioBuffer.clear();
	}

	/**
	 * Should be override by subclasses, implement the actual logic
	 * 
	 * @param source
	 * @param message
	 * @param recorder
	 * @return
	 */
	protected void processEach(Pipe source, DataMessage message,
			MessageRecorder recorder) {

	}

	/**
	 * Should be override by subclasses, implement the actual logic
	 * 
	 * @param recorder
	 */
	protected void processNew(MessageRecorder recorder) {

	}

	/**
	 * Should be override by subclasses, implement the actual logic
	 * 
	 * @param recorder
	 */
	protected void processSummary(MessageRecorder recorder) {

	}

	public NodeState getState() {
		return stateMachine.getState();
	}

	/**
	 * Callback
	 */
	protected void beforeProcess(Map<Pipe, List<DataMessage>> events) {

	}

	/**
	 * Callback
	 */
	protected void afterProcess(Map<Pipe, List<DataMessage>> events,
			List<ProcessResult> result) {

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

	public <T> T getLocation() {
		return null;
	}

	public int getPower() {
		return power;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public void setPower(int power) {
		this.power = power;
	}

	protected void fireMessageSent(DataMessage message) {
		NodeMessageEvent event = new NodeMessageEvent(this, message);
		for (NodeMessageListener listener : listenerDelegate
				.getListeners(NodeMessageListener.class))
			listener.messageSent(event);
	}

	protected void fireMessageTimeout() {
		NodeMessageEvent event = new NodeMessageEvent(this, null);
		for (NodeMessageListener listener : listenerDelegate
				.getListeners(NodeMessageListener.class))
			listener.messageTimeout(event);
	}

	protected void fireMessageReceived(DataMessage message) {
		NodeMessageEvent event = new NodeMessageEvent(this, message);
		for (NodeMessageListener listener : listenerDelegate
				.getListeners(NodeMessageListener.class))
			listener.messageReceived(event);
	}

	protected void fireStateChange(NodeStateEvent event) {
		for (NodeStateListener listener : listenerDelegate
				.getListeners(NodeStateListener.class))
			listener.stateChanged(event);
	}

	public <EL extends EventListener> void addListener(Class<EL> clazz,
			EL listener) {
		listenerDelegate.addListener(clazz, listener);
	}

	public <EL extends EventListener> void removeListener(Class<EL> clazz,
			EL listener) {
		listenerDelegate.removeListener(clazz, listener);
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

	public static class ProcessResult {

		private long cpuTime;

		private long ioTime;

		private Pipe pipe;

		private DataMessage message;

		public ProcessResult(long cpu, long io, Pipe p, DataMessage m) {
			Validate.isTrue(cpu > 0 && io >= 0 && cpu + io > 0);
			this.cpuTime = cpu;
			this.ioTime = io;
			this.pipe = p;
			this.message = m;
		}

		public void cpuDecrease() {
			cpuTime--;
		}

		public void ioDecrease() {
			ioTime--;
		}

		public boolean cpuReady() {
			return cpuTime == 0;
		}

		public boolean ready() {
			return cpuTime == 0 && ioTime == 0;
		}

		public long getCpuTime() {
			return cpuTime;
		}

		public long getIoTime() {
			return ioTime;
		}

		public Pipe getPipe() {
			return pipe;
		}

		public DataMessage getMessage() {
			return message;
		}

		public String toString() {
			return MessageFormat.format("[{0},{1},{2}]", cpuTime, ioTime,
					message.toString());
		}
	}

	public class MessageRecorder {

		protected List<ProcessResult> storage;

		protected Logger logger = LoggerFactory.getLogger(Node.this.getClass());

		public MessageRecorder() {
			storage = new ArrayList<ProcessResult>();
		}

		public void record(Pipe pipe, DataMessage message) {
			record(1l, 0l, pipe, message);
		}

		public void record(Long time, Pipe pipe, DataMessage message) {
			record(time, 0l, pipe, message);
		}

		public void record(Long cpuTime, Long ioTime, Pipe pipe,
				DataMessage message) {
			ProcessResult result = new ProcessResult(cpuTime, ioTime, pipe,
					message);
			storage.add(result);
		}

		public List<ProcessResult> summarize() {
			return storage;
		}

	}
}
