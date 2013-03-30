package edu.clarkson.gdc.simulator.framework;

import static org.junit.Assert.assertEquals;

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
		node2 = new TestNode();

		env.add(node1);
		env.add(node2);
		new Pipe(node1, node2);
	}

	@Test
	public void testProcessBusy() {
		node2.setTimeModel(new ProcessTimeModel() {
			@Override
			public long latency(Component component,
					Map<Pipe, List<DataMessage>> msgs) {
				return 2;
			}
		});
		assertEquals(NodeState.FREE, node1.getState());
		assertEquals(NodeState.FREE, node2.getState());

		env.getClock().tick();
		assertEquals(NodeState.FREE, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.BUSY, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.BUSY, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.BUSY, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.BUSY, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.FREE, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.BUSY, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.BUSY, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.FREE, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.FREE, node2.getState());
	}

	@Test
	public void testProcessFailure() {
		node2.setTimeModel(new ProcessTimeModel() {
			@Override
			public long latency(Component component,
					Map<Pipe, List<DataMessage>> msgs) {
				return 2;
			}
		});
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
		assertEquals(NodeState.BUSY, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.BUSY, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.EXCEPTION, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.FREE, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.EXCEPTION, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.BUSY, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.EXCEPTION, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.FREE, node2.getState());
		env.getClock().tick();
		assertEquals(NodeState.FREE, node2.getState());

		Map<Long, List<DataMessage>> feedback = node1.getFeedback();
		assertEquals(2,feedback.size());
		assertEquals(2,feedback.get(Long.valueOf(5l)).size());
		assertEquals(1,feedback.get(Long.valueOf(9l)).size());
	}

	@Test
	public void testSend() {
		Node node = new Node() {
			@Override
			protected List<ProcessResult> process(
					Map<Pipe, List<DataMessage>> events) {
				return null;
			}
		};

		Pipe pipe = new Pipe(node, new TestNode());
		pipe.setId("1-2");
	}

}
