package edu.clarkson.gdc.simulator.framework;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.clarkson.gdc.simulator.framework.DataMessage.PathNode;

public class DataMessageTest {

	@Test
	public void testAccess() {
		DataMessage event = new DataMessage();
		assertEquals(-1,event.getTimestamp());
		
		event.access(new PathNode(null,5));
		
		assertEquals(5,event.getTimestamp());
	}

}
