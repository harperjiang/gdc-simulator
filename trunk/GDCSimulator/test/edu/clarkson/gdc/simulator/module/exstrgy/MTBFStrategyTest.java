package edu.clarkson.gdc.simulator.module.exstrgy;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class MTBFStrategyTest {

	@Test
	public void test() {
		for (int i = 0; i < 100; i++) {
			MTBFStrategy strategy = new MTBFStrategy(0.5f, 5);
			int counter = 0;
			while (null == strategy.getException(counter++))
				;
			assertNotNull(strategy.getException(counter++));
			assertNotNull(strategy.getException(counter++));
			assertNotNull(strategy.getException(counter++));
			assertNotNull(strategy.getException(counter++));
			assertNotNull(strategy.getException(counter++));

		}
	}

}
