package edu.clarkson.gdc.simulator.framework;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

public class ClockTest {

	@Test
	public void test() {
		final Clock clock = new Clock();
		final AtomicInteger atomInt = new AtomicInteger();
		clock.register(new Stepper() {
			@Override
			public void send() {

			}

			@Override
			public void process() {
				atomInt.incrementAndGet();
			}

			@Override
			public Clock getClock() {
				return clock;
			}
		});

		clock.step();
		clock.step();

		assertEquals(2, atomInt.intValue());
	}

}
