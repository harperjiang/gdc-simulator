package edu.clarkson.gdc.simulator.impl;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.clarkson.gdc.simulator.Data;
import edu.clarkson.gdc.simulator.DataCenter;
import edu.clarkson.gdc.simulator.ExceptionStrategy;
import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.Node;
import edu.clarkson.gdc.simulator.framework.NodeException;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.impl.message.ReadKeyRequest;
import edu.clarkson.gdc.simulator.impl.message.ReadKeyResponse;

/**
 * Revision History:
 * <table border="1" style="border-collapse:collapse;" cellpadding="5">
 * <tr>
 * <th>Version</th>
 * <th>Date</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>1.0</td>
 * <td>Sep-01-2012</td>
 * <td>Implement the basic function of processing data. All data is processed as
 * soon as they are received. A latency is apply so that only after this latency
 * will the processed data be sent out. Data received at time T will be sent out
 * at time T+&lt;latency&gt;</td>
 * </tr>
 * <tr>
 * <td>1.1</td>
 * <td>Jan-20-2013</td>
 * <td>
 * <p>
 * Implement a exponential decay model. This model adjust the message latency
 * based on received message count. The processing time will increase
 * exponentially.
 * </p>
 * <p>
 * There will be 2 parameters in the scenario: processing power P and scale S,
 * which satisfies:<br/>
 * t = exp(S*&lt;message_count&gt;)/P
 * </p>
 * </td>
 * </tr>
 * </table>
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.1
 * 
 */
public class DefaultDataCenter extends Node implements DataCenter {

	private ExceptionStrategy exceptionStrategy;

	public ExceptionStrategy getExceptionStrategy() {
		return exceptionStrategy;
	}

	public void setExceptionStrategy(ExceptionStrategy exceptionStrategy) {
		this.exceptionStrategy = exceptionStrategy;
	}

	private Point2D location;

	public Point2D getLocation() {
		return location;
	}

	public void setLocation(Point2D location) {
		this.location = location;
	}

	@Override
	protected List<ProcessResult> process(Map<Pipe, List<DataMessage>> events) {
		ProcessResult success = new ProcessResult();
		ProcessResult failed = new ProcessResult();

		List<ProcessResult> results = new ArrayList<ProcessResult>();
		results.add(success);
		results.add(failed);

		// Read Request
		for (Entry<Pipe, List<DataMessage>> entry : events.entrySet()) {
			Pipe pipe = entry.getKey();
			if (pipe.getDestination().equals(this)) {
				for (DataMessage message : entry.getValue()) {
					// Read Key Request
					if (message instanceof ReadKeyRequest) {
						ReadKeyRequest request = (ReadKeyRequest) message;
						ReadKeyResponse resp = new ReadKeyResponse(
								(ReadKeyRequest) message);
						resp.setLoad(read(request.getKey()));
						success.add(pipe, resp);
					}

					// {Add Other Request here}
				}
			}
		}
		return results;
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
