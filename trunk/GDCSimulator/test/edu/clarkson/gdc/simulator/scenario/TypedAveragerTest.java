package edu.clarkson.gdc.simulator.scenario;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;

import org.junit.Test;

public class TypedAveragerTest {

	@Test
	public void test() {
		TypedAverager ta = new TypedAverager();

		ta.start();

		ta.add("A", BigDecimal.TEN);
		ta.add("A", new BigDecimal("5"));
		ta.add("B", new BigDecimal("3"));
		ta.add("C", new BigDecimal("7"));

		ta.stop();

		assertNull(ta.percent("CW"));
		assertEquals(new BigDecimal("0.5000"), ta.percent("A"));
		assertEquals(new BigDecimal("0.2500"), ta.percent("B"));
		assertEquals(new BigDecimal("0.2500"), ta.percent("C"));
		assertEquals(new BigDecimal("6.2500"),ta.average(null));
		assertEquals(new BigDecimal("7.5000"),ta.average("A"));
		assertEquals(new BigDecimal("3.0000"),ta.average("B"));
		assertEquals(new BigDecimal("7.0000"),ta.average("C"));
	}

}
