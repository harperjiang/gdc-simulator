package edu.clarkson.gdc.simulator.framework;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class NodeTest {

	@Test
	public void testProcess() {

	}

	@Test
	public void testSend() {
		Node node = new Node() {
			@Override
			protected ProcessGroup process(Map<Pipe, List<DataMessage>> events) {
				return null;
			}
		};

		Pipe pipe = new Pipe(node, null);
		pipe.setId("1-2");
	}

}
