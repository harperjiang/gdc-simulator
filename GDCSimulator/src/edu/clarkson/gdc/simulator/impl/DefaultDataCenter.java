package edu.clarkson.gdc.simulator.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.clarkson.gdc.simulator.Data;
import edu.clarkson.gdc.simulator.DataCenter;
import edu.clarkson.gdc.simulator.FailureStrategy;
import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.Node;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.impl.message.ReadKeyFail;
import edu.clarkson.gdc.simulator.impl.message.ReadKeyRequest;
import edu.clarkson.gdc.simulator.impl.message.ReadKeyResponse;

public class DefaultDataCenter extends Node implements DataCenter {

	private FailureStrategy failureStrategy;

	public FailureStrategy getFailureStrategy() {
		return failureStrategy;
	}

	public void setFailureStrategy(FailureStrategy failureStrategy) {
		this.failureStrategy = failureStrategy;
	}

	@Override
	protected ProcessGroup process(Map<Pipe, List<DataMessage>> events) {
		ProcessResult success = new ProcessResult();
		ProcessResult failed = new ProcessResult();

		ProcessGroup group = new ProcessGroup(success, failed);

		// Read Request
		for (Entry<Pipe, List<DataMessage>> entry : events.entrySet()) {
			Pipe pipe = entry.getKey();
			if (pipe.getDestination().equals(this)) {
				for (DataMessage message : entry.getValue()) {
					// Read Key Request
					if (message instanceof ReadKeyRequest) {
						ReadKeyRequest request = (ReadKeyRequest) message;
						if (getFailureStrategy().shouldFail()) {
							ReadKeyFail resp = new ReadKeyFail(
									(ReadKeyRequest) message);
							failed.add(pipe, resp);
						} else {
							ReadKeyResponse resp = new ReadKeyResponse(
									(ReadKeyRequest) message);
							resp.setLoad(read(request.getKey()));
							success.add(pipe, resp);
						}
					}
					// Add Other Request here
				}
			}
		}
		return group;
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

}
