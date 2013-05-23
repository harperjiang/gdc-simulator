package edu.clarkson.gdc.simulator.module.exstrgy;

import java.util.Random;

import org.apache.commons.lang.Validate;

import edu.clarkson.gdc.simulator.ExceptionStrategy;
import edu.clarkson.gdc.simulator.framework.NodeException;
import edu.clarkson.gdc.simulator.framework.StrategyException;

public class FailureRateStrategy implements ExceptionStrategy {

	private double rate;

	private Random random;

	public FailureRateStrategy(double p) {
		Validate.isTrue(p <= 1 && p >= 0,
				"The possibility should be between 0 and 1");
		this.rate = p;
		this.random = new Random(hashCode() * System.currentTimeMillis());
	}

	@Override
	public NodeException getException(long tick) {
		return random.nextDouble() > rate ? null
				: new StrategyException();
	}

}
