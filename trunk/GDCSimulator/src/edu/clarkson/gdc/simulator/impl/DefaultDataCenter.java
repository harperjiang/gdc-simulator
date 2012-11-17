package edu.clarkson.gdc.simulator.impl;

import java.util.List;
import java.util.Map;

import edu.clarkson.gdc.simulator.Data;
import edu.clarkson.gdc.simulator.DataCenter;
import edu.clarkson.gdc.simulator.FailureStrategy;
import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.Node;
import edu.clarkson.gdc.simulator.framework.Pipe;

public class DefaultDataCenter extends Node implements DataCenter {

	@Override
	public FailureStrategy getFailureStrategy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Data read(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void write(Data data) {
		// TODO Auto-generated method stub

	}

	@Override
	protected ProcessResult process(Map<Pipe, List<DataMessage>> events) {
		// TODO Auto-generated method stub
		return null;
	}
}
