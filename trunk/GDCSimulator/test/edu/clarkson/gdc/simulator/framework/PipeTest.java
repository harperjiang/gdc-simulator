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

		DataMessage event = new DataMessage();

		Node src = new TestNode();
		Node dest = new TestNode();
		Pipe pipe = new Pipe(src, dest);

		assertNotNull(pipe.getClock());

		pipe.setLatency(4);
		pipe.put(src, event);

		assertTrue(CollectionUtils.isEmpty(pipe.get(dest)));
		Clock.getInstance().step();
		assertTrue(CollectionUtils.isEmpty(pipe.get(dest)));
		Clock.getInstance().step();
		assertTrue(CollectionUtils.isEmpty(pipe.get(dest)));
		Clock.getInstance().step();
		assertTrue(CollectionUtils.isEmpty(pipe.get(dest)));
		Clock.getInstance().step();
		List<DataMessage> events = pipe.get(dest);
		assertTrue(!CollectionUtils.isEmpty(events));
		assertEquals(1, events.size());
		assertEquals(event, events.get(0));
	}

	@Test
	public void testPut() {
		DataMessage event = new DataMessage();
		Node src = new TestNode();
		Node dest = new TestNode();
		Pipe pipe = new Pipe(src, dest);

		assertNotNull(pipe.getClock());

		pipe.setLatency(4);
		pipe.put(src, event);
		assertEquals(event.getTimestamp(), Clock.getInstance().getCounter()
				+ pipe.getLatency());
	}

}
