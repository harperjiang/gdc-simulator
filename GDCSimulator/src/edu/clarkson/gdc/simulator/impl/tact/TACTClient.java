package edu.clarkson.gdc.simulator.impl.tact;

import java.text.MessageFormat;
import java.util.Random;

import edu.clarkson.gdc.simulator.Client;
import edu.clarkson.gdc.simulator.framework.Node;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.impl.simple.DefaultData;
import edu.clarkson.gdc.simulator.impl.tact.message.ClientRead;
import edu.clarkson.gdc.simulator.impl.tact.message.ClientWrite;

public class TACTClient extends Node implements Client {

	private double readRatio = 0.5;

	private long interval = 10;

	private Random random = new Random(System.currentTimeMillis() * hashCode());

	/**
	 * Randomly generate a read/write request on a given ratio
	 */
	@Override
	protected void processNew(MessageRecorder recorder) {
		if (0 != random.nextInt((int) interval))
			return;
		if (random.nextDouble() < readRatio) {
			recorder.record(0l, getServerPipe(), new ClientRead());
			if (logger.isDebugEnabled()) {
				logger.debug(MessageFormat.format(
						"Client send read to {0} at {1}", getServerPipe()
								.getOpponent(this), getClock().getCounter()));
			}
		} else {
			recorder.record(0l, getServerPipe(), new ClientWrite(
					new DefaultData("key")));
			if (logger.isDebugEnabled()) {
				logger.debug(MessageFormat.format(
						"Client send write to {0} at {1}", getServerPipe()
								.getOpponent(this), getClock().getCounter()));
			}
		}
	}

	private Pipe serverPipe;

	/**
	 * TACT client should connect to only one server
	 * 
	 * @return
	 */
	protected Pipe getServerPipe() {
		if (null != serverPipe)
			return serverPipe;
		if (getPipes().size() != 1)
			throw new IllegalArgumentException(
					"TACT Client should have only one connection");
		serverPipe = getPipes().values().iterator().next();
		if (!(serverPipe.getOpponent(this) instanceof TACTDataCenter)) {
			throw new IllegalArgumentException(
					"TACT Client can only connect to TACT DataCenter");
		}
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

}
