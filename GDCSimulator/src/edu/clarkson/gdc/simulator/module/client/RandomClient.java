package edu.clarkson.gdc.simulator.module.client;

import java.util.Iterator;
import java.util.Random;

import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.Pipe;

/**
 * Random Client employs random numbers to help determine the interval and
 * read/write operation issued. It is ideal for generating data for test.
 * 
 * 
 * 
 * @author harper
 * @since GDCSimulator 1.0
 * @version 1.0
 * 
 * 
 */
public abstract class RandomClient extends AbstractClient {

	protected double readRatio = 1;

	protected long interval = 25;

	protected long timeout = -1;

	protected boolean waitResponse = false;

	private Random random = new Random(System.currentTimeMillis() * hashCode());

	private transient boolean waiting = false;

	private transient long start;

	@Override
	protected void processNew(MessageRecorder recorder) {
		if (!(waitResponse && waiting)) {
			if (0 != random.nextInt((int) interval))
				return;
			if (random.nextDouble() < readRatio) {
				genRead(recorder);
			} else {
				genWrite(recorder);
			}
			waiting = true;
			start = getClock().getCounter();
		} else {
			if (timeout != -1 && getClock().getCounter() - start > timeout) {
				fireMessageTimeout();
				waiting = false;
			}
		}
	}

	protected abstract void genRead(MessageRecorder recorder);

	protected abstract void genWrite(MessageRecorder recorder);

	/**
	 * Client should override this to implement customized judge function
	 * 
	 * @param pipe
	 * @param message
	 * @return
	 */
	protected boolean isResponse(Pipe pipe, DataMessage message) {
		return true;
	}

	@Override
	protected void processEach(Pipe source, DataMessage message,
			MessageRecorder recorder) {
		if (waitResponse && waiting && isResponse(source, message))
			waiting = false;
	}

	private Pipe serverPipe;

	protected Pipe getServerPipe() {
		if (null != serverPipe)
			return serverPipe;
		if (getPipes().size() != 2)
			throw new IllegalArgumentException(
					"Client should have only one connection");
		Iterator<Pipe> it = getPipes().values().iterator();
		while (it.hasNext()) {
			Pipe pipe = it.next();
			if (pipe.getOpponent(this) != this) {
				serverPipe = pipe;
				break;
			}
		}
		// if (!(serverPipe.getOpponent(this) instanceof DataCenter)) {
		// throw new IllegalArgumentException(
		// "Client can only connect to DataCenter");
		// }
		return serverPipe;
	}

	public double getReadRatio() {
		return readRatio;
	}

	public void setReadRatio(double readRatio) {
		this.readRatio = readRatio;
	}

	public long getInterval() {
		return interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	public boolean isWaitResponse() {
		return waitResponse;
	}

	public void setWaitResponse(boolean waitResponse) {
		this.waitResponse = waitResponse;
	}

	public boolean isWaiting() {
		return waiting;
	}

	public void setWaiting(boolean waiting) {
		this.waiting = waiting;
	}

}
