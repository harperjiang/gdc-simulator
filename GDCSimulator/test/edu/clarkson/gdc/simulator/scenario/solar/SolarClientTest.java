package edu.clarkson.gdc.simulator.scenario.solar;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.clarkson.gdc.simulator.scenario.solar.SolarClient;
import edu.clarkson.gdc.simulator.scenario.solar.SolarClient2;

public class SolarClientTest extends SolarClient2 {

	@Test
	public void testDistance() {
		assertEquals(7758, SolarClient.distance(31066, 23308));
		assertEquals(5552, SolarClient.distance(31066, 36618));
	}

}
