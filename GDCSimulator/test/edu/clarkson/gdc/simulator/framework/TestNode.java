package edu.clarkson.gdc.simulator.framework;

import java.util.List;
import java.util.Map;

public class TestNode extends Node {

	@Override
	public void send() {
		// DO NOTHING
	}

	@Override
	public void process() {
		// DO NOTHING
	}

	@Override
	protected ProcessResult process(Map<Pipe, List<DataMessage>> events) {
		return null;
	}

}
