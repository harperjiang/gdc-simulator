package edu.clarkson.gdc.simulator.impl;

import java.util.List;
import java.util.Map;

import edu.clarkson.gdc.simulator.IndexService;
import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.Node;
import edu.clarkson.gdc.simulator.framework.Pipe;

public class DefaultIndexService extends Node implements IndexService {

	@Override
	public String locate(String key, String location) {
		return null;
	}

	@Override
	protected ProcessGroup process(Map<Pipe, List<DataMessage>> events) {
		return null;
	}
}