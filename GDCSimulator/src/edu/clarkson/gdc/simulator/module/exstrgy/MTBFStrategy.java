package edu.clarkson.gdc.simulator.module.exstrgy;

import java.util.Random;

import org.apache.commons.lang.Validate;

import edu.clarkson.gdc.simulator.framework.ExceptionStrategy;
import edu.clarkson.gdc.simulator.framework.NodeException;
import edu.clarkson.gdc.simulator.framework.StrategyException;

public class MTBFStrategy implements ExceptionStrategy {

	private double rate;

	private long mttr;

	private transient long start;

	private Random random;

	public MTBFStrategy(double p, long mttr) {
		Validate.isTrue(p <= 1 && p >= 0,
				"The possibility should be between 0 and 1");
		this.rate = p;
		this.mttr = mttr;
		this.random = new Random(hashCode() * System.currentTimeMillis());
	}

	@Override
	public NodeException getException(long tick) {
		if (tick <= start + mttr)
			return new StrategyException();
		NodeException exception = random.nextDouble() > rate ? null
				: new StrategyException();
		if (exception != null) {
			start = tick;
		}
		return exception;
	}

}
