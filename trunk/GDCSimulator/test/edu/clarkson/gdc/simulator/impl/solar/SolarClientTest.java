package edu.clarkson.gdc.simulator.impl.solar;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SolarClientTest extends SolarClient {

	@Test
	public void testDistance() {
		assertEquals(7758, SolarClient.distance(31066, 23308));
		assertEquals(5552, SolarClient.distance(31066, 36618));
	}

}
