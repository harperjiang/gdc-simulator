package edu.clarkson.gdc.simulator.impl.tact;

import java.util.ArrayList;
import java.util.List;

import edu.clarkson.gdc.simulator.Data;
import edu.clarkson.gdc.simulator.common.Pair;
import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.framework.ResponseMessage;
import edu.clarkson.gdc.simulator.framework.storage.Storage;
import edu.clarkson.gdc.simulator.impl.AbstractDataCenter;
import edu.clarkson.gdc.simulator.impl.tact.message.ClientRead;
import edu.clarkson.gdc.simulator.impl.tact.message.ClientResponse;
import edu.clarkson.gdc.simulator.impl.tact.message.ClientWrite;
import edu.clarkson.gdc.simulator.impl.tact.message.ServerPull;
import edu.clarkson.gdc.simulator.impl.tact.message.ServerPullResponse;
import edu.clarkson.gdc.simulator.impl.tact.message.ServerPush;

public class TACTDataCenter extends AbstractDataCenter {

	private int number;

	// For debug purpose
	private SortingList<Operation> committed;

	// Tentative operations
	private SortingList<Operation> tentative;

	public TACTDataCenter(int number, int total) {
		super();
		this.number = number;
		timeVector = new Long[total];
		for (int i = 0; i < timeVector.length; i++)
			timeVector[i] = 0l;

		pulling = new boolean[total];
		pushed = new long[total];

		committed = new SortingList<Operation>();
		tentative = new SortingList<Operation>();

		buffer = new ArrayList<Pair<Pipe, DataMessage>>();
	}

	private List<Pair<Pipe, DataMessage>> buffer;

	@Override
	protected void processNew(MessageRecorder recorder) {
		// Process pending requests
		if (!isPulling() && buffer.size() != 0) {
			for (Pair<Pipe, DataMessage> pair : buffer) {
				processEach(pair.getA(), pair.getB(), recorder);
			}
			buffer.clear();
		}
	}

	@Override
	protected void processEach(Pipe source, DataMessage message,
			MessageRecorder recorder) {
		if (message instanceof ClientRead) {
			if (isPulling()) {
				buffer.add(new Pair<Pipe, DataMessage>(source, message));
			} else {
				// Consume some time
				Pair<Long, Data> read = storage.read(String.valueOf(message
						.getLoad()));
				ResponseMessage response = new ClientResponse(message);
				response.setLoad(read.getB());
				recorder.record(read.getA(), source, response);
			}
		}
		if (message instanceof ClientWrite) {
			if (isPulling()) {
				buffer.add(new Pair<Pipe, DataMessage>(source, message));
			} else {
				// Add to tentative list
				Operation operation = new Operation(getClock().getCounter(),
						number, message);
				tentative.add(operation);

				timeVector[number] = getClock().getCounter();
				// Consume some time
				Data data = message.getLoad();
				long time = storage.write(data);
				recorder.record(time, source, new ClientResponse(message));
			}
		}
		if (message instanceof ServerPush) {
			ServerPush push = (ServerPush) message;
			// Update the server timestamp that send this push
			timeVector[push.getServerNum()] = push.getEndPoint();
			// Add all the events to tentative list
			for (Operation opr : push.getOperations())
				tentative.add(opr);
		}
		if (message instanceof ServerPull) {
			ServerPull pull = (ServerPull) message;

			ServerPullResponse response = new ServerPullResponse(pull);
			response.setServerNum(number);
			List<Operation> oprs = new ArrayList<Operation>();
			oprs.addAll(getCommitted(pull.getStartPoint()));
			oprs.addAll(getTentative(pull.getStartPoint()));
			response.setOperations(oprs);
			// TODO Decide the response time
			recorder.record(0l, source, response);
		}
		if (message instanceof ServerPullResponse) {
			ServerPullResponse response = (ServerPullResponse) message;
			pulling[response.getServerNum()] = false;
			timeVector[response.getServerNum()] = response.getEndPoint();
			// Write the information get from response to storage
			for (Operation opr : response.getOperations())
				tentative.add(opr);
		}

	}

	@Override
	protected void processSummary(MessageRecorder recorder) {
		// Do commit operations if possible
		long minPos = Long.MAX_VALUE;
		for (long time : timeVector) {
			minPos = Math.min(time, minPos);
		}
		while (true) {
			if (tentative.getList().isEmpty())
				break;
			if (tentative.getList().get(0).getTimestamp().getTime() > minPos)
				break;
			Operation opr = tentative.getList().remove(0);
			committed.add(opr);
		}

		// Check absolute numerical error & staleness
		int threshold = getNumError() / (timeVector.length - 1);

		for (Pipe pipe : broadcastPipes()) {
			TACTDataCenter oppo = (TACTDataCenter) pipe.getOpponent(this);
			List<Operation> pushOprs = new ArrayList<Operation>();
			pushOprs.addAll(getCommitted(pushed[oppo.number]));
			pushOprs.addAll(getTentative(pushed[oppo.number]));
			long pushMax = 0;
			for (Operation opr : pushOprs) {
				pushMax = Math.max(pushMax, opr.getTimestamp().getTime());
			}
			if (pushOprs.size() >= threshold
					|| getClock().getCounter() - timeVector[oppo.number] > getStaleness()) {
				pushed[oppo.number] = pushMax;
				// Push
				ServerPush pushRequest = new ServerPush();
				pushRequest.setOperations(pushOprs);
				pushRequest.setServerNum(number);
				recorder.record(getPushReactionTime(), pipe, pushRequest);
			}
		}

		// Check order error
		if (tentative.getList().size() > getOrderError() && !isPulling()) {
			for (Pipe pipe : broadcastPipes()) {
				ServerPull pull = new ServerPull();
				TACTDataCenter oppo = (TACTDataCenter) pipe.getOpponent(this);
				pull.setServerNum(oppo.number);
				pull.setStartPoint(timeVector[oppo.number]);
				recorder.record(getPullReactionTime(), pipe, pull);
			}
			setPulling();
		}
	}

	protected List<Operation> getCommitted(long startPoint) {
		List<Operation> result = new ArrayList<Operation>();
		for (Operation opr : committed.getList()) {
			if (opr.getTimestamp().getServerNum() == number
					&& opr.getTimestamp().getTime() > startPoint)
				result.add(opr);
		}
		return result;
	}

	protected List<Operation> getTentative(long startPoint) {
		List<Operation> result = new ArrayList<Operation>();
		for (Operation opr : tentative.getList()) {
			if (opr.getTimestamp().getServerNum() == number
					&& opr.getTimestamp().getTime() > startPoint)
				result.add(opr);
		}
		return result;
	}

	private boolean[] pulling;

	private long[] pushed;

	protected boolean isPulling() {
		for (boolean p : pulling)
			if (p)
				return true;
		return false;
	}

	protected void setPulling() {
		for (int i = 0; i < pulling.length; i++)
			pulling[i] = true;
	}

	protected List<Pipe> broadcastPipes() {
		List<Pipe> pipes = new ArrayList<Pipe>();
		for (Pipe pipe : getPipes().values()) {
			if (pipe.getOpponent(this) instanceof TACTDataCenter) {
				pipes.add(pipe);
			}
		}
		return pipes;
	}

	private Storage storage;

	public Storage getStorage() {
		return storage;
	}

	public void setStorage(Storage storage) {
		this.storage = storage;
	}

	private Long[] timeVector;

	private int numError;

	private int orderError;

	private long staleness;

	private long pushReactionTime;

	private long pullReactionTime;

	public int getNumError() {
		return numError;
	}

	public void setNumError(int numError) {
		this.numError = numError;
	}

	public int getOrderError() {
		return orderError;
	}

	public void setOrderError(int orderError) {
		this.orderError = orderError;
	}

	public long getStaleness() {
		return staleness;
	}

	public void setStaleness(long staleness) {
		this.staleness = staleness;
	}

	public long getPushReactionTime() {
		return pushReactionTime;
	}

	public void setPushReactionTime(long pushReactionTime) {
		this.pushReactionTime = pushReactionTime;
	}

	public long getPullReactionTime() {
		return pullReactionTime;
	}

	public void setPullReactionTime(long pullReactionTime) {
		this.pullReactionTime = pullReactionTime;
	}

	public String toString() {
		return "TACTDataCenter " + number;
	}
}
