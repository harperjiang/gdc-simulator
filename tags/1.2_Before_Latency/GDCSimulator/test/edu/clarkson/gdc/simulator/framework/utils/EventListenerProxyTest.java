package edu.clarkson.gdc.simulator.framework.utils;

import static org.junit.Assert.assertNotNull;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import edu.clarkson.gdc.simulator.framework.NodeState;
import edu.clarkson.gdc.simulator.framework.NodeStateEvent;
import edu.clarkson.gdc.simulator.framework.NodeStateListener;
import edu.clarkson.gdc.simulator.framework.TestNode;

public class EventListenerProxyTest {

	@Test
	public void testWork() {
		final AtomicReference<NodeStateEvent> ref = new AtomicReference<NodeStateEvent>();
		EventListenerProxy proxy = new EventListenerProxy();
		proxy.addListener(NodeStateListener.class, new NodeStateListener() {
			@Override
			public void stateChanged(NodeStateEvent event) {
				ref.set(event);
			}
		});

		NodeStateListener listener = proxy.getProbe(NodeStateListener.class);
		listener.stateChanged(new NodeStateEvent(new TestNode(),
				NodeState.FREE, NodeState.BUSY));

		assertNotNull(ref.get());
	}
}
