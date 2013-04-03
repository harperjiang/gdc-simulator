package edu.clarkson.gdc.simulator.framework;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.clarkson.gdc.simulator.ExceptionStrategy;

public class NodeTest {

	private Environment env;

	private TestNode node1;

	private TestNode node2;

	@Before
	public void prepare() {
		env = new Environment();
		node1 = new TestNode() {
			{
				setId("source");
			}

			@Override
			public void send() {
				// Send a dummy message to each pipe
				if (getClock().getCounter() == 1
						|| getClock().getCounter() == 2
						|| getClock().getCounter() == 6)
					for (Pipe pipe : getPipes().values()) {
						DataMessage msg = new DataMessage();
						pipe.put(this, msg);
					}

			}
		};
		node2 = new TestNode() {
			{
				capacity = 1;
				setId("dest");
			}
		};

		env.add(node1);
		env.add(node2);
		new Pipe(node1, node2);
	}

	@Test
	public void testProcessBusy() {
		node2.setExceptionStrategy(null);
		assertEquals(NodeState.FREE, node1.getState());
		assertEquals(NodeState.FREE, node2.getState());

		env.getClock().tick();
		assertEquals(NodeState.FREE, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.FREE, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.BUSY, node2.getState());
		env.getClock().tick();
		assertTrue(NodeState.FREE == node2.getState()
				|| NodeState.BUSY == node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.FREE, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.FREE, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.FREE, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.FREE, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.FREE, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.FREE, node2.getState());

		Map<Long, List<DataMessage>> feedback = node1.getFeedback();
		assertTrue(feedback.containsKey(5l) || feedback.containsKey(6l)
				|| feedback.containsKey(11l));
	}

	@Test
	public void testProcessFailure() {
		node2.setExceptionStrategy(new ExceptionStrategy() {
			@Override
			public NodeException getException(long tick) {
				if (tick == 4 || tick == 6 || tick == 8)
					return new StrategyException();
				return null;
			}
		});

		assertEquals(NodeState.FREE, node1.getState());
		assertEquals(NodeState.FREE, node2.getState());

		env.getClock().tick();
		assertEquals(NodeState.FREE, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.FREE, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.BUSY, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.EXCEPTION, node2.getState());
		env.getClock().tick();
		assertTrue(NodeState.FREE == node2.getState()
				|| NodeState.BUSY == node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.EXCEPTION, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.BUSY, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.EXCEPTION, node2.getState());
		env.getClock().tick();
		assertTrue(NodeState.FREE == node2.getState()
				|| NodeState.BUSY == node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.FREE, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.FREE, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.FREE, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.FREE, node2.getState());

		Map<Long, List<DataMessage>> feedback = node1.getFeedback();
		assertTrue(feedback.containsKey(6l) || feedback.containsKey(8l)
				|| feedback.containsKey(10l) || feedback.containsKey(11l)
				|| feedback.containsKey(12l));
	}

	@Test
	public void testSend() {
		Node node = new Node() {
		};

		Pipe pipe = new Pipe(node, new TestNode());
		pipe.setId("1-2");
	}

}
