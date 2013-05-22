package edu.clarkson.gdc.simulator.scenario.latency.tact;

import java.util.ArrayList;
import java.util.List;

import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.framework.ResponseMessage;
import edu.clarkson.gdc.simulator.framework.SelfPipe;
import edu.clarkson.gdc.simulator.module.message.ClientRead;
import edu.clarkson.gdc.simulator.module.message.ClientResponse;
import edu.clarkson.gdc.simulator.module.message.ClientWrite;
import edu.clarkson.gdc.simulator.module.server.AbstractDataCenter;
import edu.clarkson.gdc.simulator.scenario.latency.tact.message.ServerPull;
import edu.clarkson.gdc.simulator.scenario.latency.tact.message.ServerPullResponse;
import edu.clarkson.gdc.simulator.scenario.latency.tact.message.ServerPush;

/**
 * Time Consumption:
 * 
 * Memory: 5 General Read: 10 General Write: 20 Sized Read: 2* size Sized Write:
 * 5*size
 * 
 * 
 * 
 * @author harper
 * @since GDCSimulator 1.0
 * @version 1.0
 * 
 * 
 */
public class TACTDataCenter extends AbstractDataCenter {

	private long readcpu = 10l;
	private long writecpu = 15l;
	private long readio = 10l;
	private long writeio = 10l;

	private int index;

	private TACTCore core;

	public TACTDataCenter(int ind, int total) {
		super();
		this.index = ind;
		core = new TACTCore(index, total);
	}

	@Override
	protected void processNew(MessageRecorder recorder) {
	}

	@Override
	protected void processEach(Pipe source, DataMessage message,
			MessageRecorder recorder) {
		if (message instanceof ClientRead) {
			// Consume some time
			ResponseMessage response = new ClientResponse(message);
			recorder.record(readcpu, readio, source, response);
		}
		if (message instanceof ClientWrite) {
			// Add new operation
			Operation operation = new Operation(getClock().getCounter(), index,
					message);
			core.add(operation);
			recorder.record(writecpu, writeio, source, new ClientResponse(
					message));
		}
		if (message instanceof ServerPush) {
			ServerPush push = (ServerPush) message;
			// Update the server timestamp that send this push
			core.receive(push.getServerNum(), push.getEndPoint(),
					push.getOperations());
			recorder.record(writecpu, writeio, getSelfPipe(), new DummyMessage(
					"Process Server Push"));
		}
		if (message instanceof ServerPull) {
			ServerPull pull = (ServerPull) message;
			ServerPullResponse response = new ServerPullResponse(pull);
			long endPoint = getClock().getCounter() - 1;
			response.setServerNum(index);
			List<Operation> oprs = core.bepulled(pull.getServerNum());
			response.setOperations(oprs);
			response.setEndPoint(endPoint);
			recorder.record(readcpu, readio, source, response);
		}
		if (message instanceof ServerPullResponse) {
			ServerPullResponse response = (ServerPullResponse) message;
			core.receivepull(response.getServerNum(), response.getEndPoint(),
					response.getOperations());
			recorder.record(writecpu, writeio, getSelfPipe(), new DummyMessage(
					"Process Server Pull Response"));
		}
	}

	@Override
	protected void processSummary(MessageRecorder recorder) {
		// Do commit operations if possible
		if (core.commit()) {
			recorder.record(writecpu, writeio, getSelfPipe(), new DummyMessage(
					"Process Commit"));
		}

		// Check absolute numerical error
		if (getNumError() >= 0 || getStaleness() >= 0) {
			for (Pipe pipe : broadcastPipes()) {
				TACTDataCenter tdc = (TACTDataCenter) pipe.getOpponent(this);
				if (core.hasNumError(tdc.index)
						|| core.isStale(tdc.index, getClock().getCounter())) {
					List<Operation> pushOprs = core.push(tdc.index);
					// Push
					ServerPush pushRequest = new ServerPush();
					pushRequest.setOperations(pushOprs);
					pushRequest.setServerNum(index);
					pushRequest.setEndPoint(getClock().getCounter() - 1);
					recorder.record(readcpu, readio, pipe, pushRequest);
				}
			}
		}

		// Check order error
		if (!core.isPulling() && core.hasOrderError()) {
			for (Pipe pipe : broadcastPipes()) {
				ServerPull pull = new ServerPull();
				TACTDataCenter oppo = (TACTDataCenter) pipe.getOpponent(this);
				pull.setServerNum(oppo.index);
				pull.setStartPoint(core.getTime(oppo.index));
				recorder.record(readcpu, readio, pipe, pull);
			}
			core.pull();
		}

		core.summarize();
	}

	private List<Pipe> broadcastPipes;

	protected List<Pipe> broadcastPipes() {
		if (null != broadcastPipes)
			return broadcastPipes;
		broadcastPipes = new ArrayList<Pipe>();
		for (Pipe pipe : getPipes().values()) {
			if (pipe.getOpponent(this) instanceof TACTDataCenter
					&& !(pipe instanceof SelfPipe)) {
				broadcastPipes.add(pipe);
			}
		}
		return broadcastPipes;
	}

	public int getNumError() {
		return core.getNumerr();
	}

	public void setNumError(int numError) {
		core.setNumerr(numError);
	}

	public int getOrderError() {
		return core.getOrdererr();
	}

	public void setOrderError(int orderError) {
		core.setOrdererr(orderError);
	}

	public long getStaleness() {
		return core.getStaleness();
	}

	public void setStaleness(long staleness) {
		core.setStaleness(staleness);
	}

	public String toString() {
		return "TACTDataCenter " + index;
	}
}
