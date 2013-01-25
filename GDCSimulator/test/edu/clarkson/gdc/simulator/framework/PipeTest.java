package edu.clarkson.gdc.simulator.framework;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import edu.clarkson.gdc.simulator.framework.ProcessTimeModel.ConstantTimeModel;

public class PipeTest {

	@Test
	public void testGet() {

		DataMessage event = new DataMessage();
		Environment env = new Environment();
		Node src = new TestNode();
		Node dest = new TestNode();
		Pipe pipe = new Pipe(src, dest);
		env.add(src);
		env.add(dest);

		assertNotNull(pipe.getClock());

		pipe.setTimeModel(new ConstantTimeModel(4));
		pipe.put(src, event);

		assertTrue(CollectionUtils.isEmpty(pipe.get(dest)));
		env.getClock().step();
		assertTrue(CollectionUtils.isEmpty(pipe.get(dest)));
		env.getClock().step();
		assertTrue(CollectionUtils.isEmpty(pipe.get(dest)));
		env.getClock().step();
		assertTrue(CollectionUtils.isEmpty(pipe.get(dest)));
		env.getClock().step();
		List<DataMessage> events = pipe.get(dest);
		assertTrue(!CollectionUtils.isEmpty(events));
		assertEquals(1, events.size());
		assertEquals(event, events.get(0));
	}

	@Test
	public void testPut() {
		DataMessage event = new DataMessage();

		Environment env = new Environment();
		Node src = new TestNode();
		Node dest = new TestNode();
		Pipe pipe = new Pipe(src, dest);
		env.add(src);
		env.add(dest);
		assertNotNull(pipe.getClock());

		pipe.setTimeModel(new ConstantTimeModel(4));
		pipe.put(src, event);
		assertEquals(event.getTimestamp(), env.getClock().getCounter() + 4);
	}

}
