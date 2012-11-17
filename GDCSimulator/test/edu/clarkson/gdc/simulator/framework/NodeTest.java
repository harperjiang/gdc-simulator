package edu.clarkson.gdc.simulator.framework;

import static org.junit.Assert.*;

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
			protected ProcessResult process(Map<Pipe, List<DataMessage>> events) {
				return null;
			}
		};
		
		Pipe pipe = new Pipe();
		pipe.setId("1-2");
		node.getInputs().add(pipe);
		
		
	}

}
