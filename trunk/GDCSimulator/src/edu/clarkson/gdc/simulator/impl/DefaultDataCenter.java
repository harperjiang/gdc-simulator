package edu.clarkson.gdc.simulator.impl;

import java.awt.geom.Point2D;
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

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 * 
 */
public class DefaultDataCenter extends Node implements DataCenter {

	private FailureStrategy failureStrategy;

	public FailureStrategy getFailureStrategy() {
		return failureStrategy;
	}

	public void setFailureStrategy(FailureStrategy failureStrategy) {
		this.failureStrategy = failureStrategy;
	}

	private Point2D location;

	public Point2D getLocation() {
		return location;
	}

	public void setLocation(Point2D location) {
		this.location = location;
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
						if (getFailureStrategy().shouldFail(
								getClock().getCounter())) {
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
					// {Add Other Request here}
				}
			}
		}
		return group;
	}

	@Override
	public Data read(final String key) {
		return new DefaultData(key);
	}

	@Override
	public void write(Data data) {
		// TODO Not Implemented
	}

	static final class DefaultData implements Data {

		private String key;

		public DefaultData(String key) {
			this.key = key;
		}

		public String getKey() {
			return key;
		}

	}

}
