package edu.clarkson.gdc.simulator.module.exstrgy;

import java.util.Random;

import org.apache.commons.lang.Validate;

import edu.clarkson.gdc.simulator.framework.ExceptionStrategy;
import edu.clarkson.gdc.simulator.framework.NodeException;
import edu.clarkson.gdc.simulator.framework.StrategyException;

public class FailureRateStrategy implements ExceptionStrategy {

	private double rate;

	private Random random;

	private long current;

	private NodeException status;

	public FailureRateStrategy(double p) {
		Validate.isTrue(p <= 1 && p >= 0,
				"The possibility should be between 0 and 1");
		this.rate = p;
		this.random = new Random(hashCode() * System.currentTimeMillis());
		this.current = -1;
	}

	@Override
	public NodeException getException(long tick) {
		if (current != tick) {
			current = tick;
			status = (random.nextDouble() > rate) ? null
					: new StrategyException();
		}
		return status;
	}
}
