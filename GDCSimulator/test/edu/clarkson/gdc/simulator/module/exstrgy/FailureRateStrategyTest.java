package edu.clarkson.gdc.simulator.module.exstrgy;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Test;

import edu.clarkson.gdc.simulator.scenario.TypedAverager;

public class FailureRateStrategyTest {

	@Test
	public void testParameter() {
		try {
			FailureRateStrategy ps = new FailureRateStrategy(1.5);
			fail("Failed to detect improper parameter");
		} catch (IllegalArgumentException e) {
		}
		try {
			FailureRateStrategy ps = new FailureRateStrategy(-1);
			fail("Failed to detect improper parameter");
		} catch (IllegalArgumentException e) {
		}
		FailureRateStrategy ps = new FailureRateStrategy(1);
		assertNotNull(ps.getException(5));
		assertNotNull(ps.getException(10));
		assertNotNull(ps.getException(15));
		assertNotNull(ps.getException(20));
	}

	@Test
	public void testRatio() {
		TypedAverager ta = new TypedAverager();
		FailureRateStrategy ps = new FailureRateStrategy(0.47d);
		ta.start();
		for (int i = 0; i < 100000; i++) {
			if (ps.getException(i) != null)
				ta.add("FAIL", BigDecimal.ZERO);
			else
				ta.add("SUCCESS", BigDecimal.ZERO);
		}
		ta.stop();
		System.out.println(ta.percent("FAIL"));
	}
}
