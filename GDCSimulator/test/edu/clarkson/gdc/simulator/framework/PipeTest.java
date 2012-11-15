package edu.clarkson.gdc.simulator.framework;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

public class PipeTest {

	@Test
	public void testGet() {

		DataEvent event = new DataEvent();
		Pipe pipe = new Pipe();

		assertNotNull(pipe.getClock());

		pipe.setLatency(4);
		pipe.put(event);

		assertTrue(CollectionUtils.isEmpty(pipe.get()));
		Clock.getInstance().step();
		assertTrue(CollectionUtils.isEmpty(pipe.get()));
		Clock.getInstance().step();
		assertTrue(CollectionUtils.isEmpty(pipe.get()));
		Clock.getInstance().step();
		assertTrue(CollectionUtils.isEmpty(pipe.get()));
		Clock.getInstance().step();
		List<DataEvent> events = pipe.get();
		assertTrue(!CollectionUtils.isEmpty(events));
	}

	@Test
	public void testPut() {
		DataEvent event = new DataEvent();
		Pipe pipe = new Pipe();

		assertNotNull(pipe.getClock());

		pipe.setLatency(4);
		pipe.put(event);
		assertEquals(event.getTimestamp(), Clock.getInstance().getCounter()
				+ pipe.getLatency());
	}

}
