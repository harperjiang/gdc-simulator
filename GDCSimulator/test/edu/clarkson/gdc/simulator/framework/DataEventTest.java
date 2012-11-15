package edu.clarkson.gdc.simulator.framework;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.clarkson.gdc.simulator.framework.DataEvent.PathNode;

public class DataEventTest {

	@Test
	public void testAccess() {
		DataEvent event = new DataEvent();
		assertEquals(-1,event.getTimestamp());
		
		event.access(new PathNode(null,5));
		
		assertEquals(5,event.getTimestamp());
	}

}
