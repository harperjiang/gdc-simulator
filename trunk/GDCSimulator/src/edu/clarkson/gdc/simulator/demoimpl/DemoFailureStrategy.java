package edu.clarkson.gdc.simulator.demoimpl;

import java.math.BigDecimal;
import java.util.Random;

import edu.clarkson.gdc.simulator.FailureStrategy;

public class DemoFailureStrategy implements FailureStrategy {

	private BigDecimal failureRate = new BigDecimal("0.05");

	public BigDecimal getFailureRate() {
		return failureRate;
	}

	public void setFailureRate(BigDecimal failureRate) {
		this.failureRate = failureRate;
	}

	@Override
	public boolean shouldFail() {
		Float rate = new Random(System.currentTimeMillis()).nextFloat();
		return rate >= failureRate.floatValue();
	}

}
