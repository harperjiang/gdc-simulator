package edu.clarkson.gdc.simulator.scenario.latency.solar;

import java.awt.geom.Point2D;

import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.ExceptionStrategy;
import edu.clarkson.gdc.simulator.framework.NodeException;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.module.server.AbstractDataCenter;

/**
 * Solar Power varies by time
 * 
 * 
 * 
 * @author harper
 * @since GDCSimulator 1.0
 * @version 1.0
 * 
 * 
 */
public class SolarServer extends AbstractDataCenter {

	protected int basepower;

	public SolarServer(String id, Point2D.Double location) {
		super();
		setId(id);
		setLocation(location);
		basepower = 100;
		setExceptionStrategy(new ExceptionStrategy() {
			private NodeException exception = new NodeException();

			@Override
			public NodeException getException(long tick) {
				if (0 == getPower()) {
					return exception;
				}
				return null;
			}
		});
	}

	@Override
	public int getPower() {
		Point2D.Double loc = getLocation();
		double timediff = loc.y * 240;
		double realtime = (getClock().getCounter() / (1000 / TimeConstant.UNIT))
				+ timediff;
		double rad = Math.abs(43200 - realtime % 86400) / 14400;
		if (rad > 1)
			return 0;
		double ratio = Math.cos(rad * Math.PI / 2);
		int res = (int) (basepower * ratio);
		return res >= 0 ? res : 0;
	}

	@Override
	protected void processEach(Pipe source, DataMessage message,
			MessageRecorder recorder) {
		if (message instanceof SolarClientRead) {
			recorder.record(1l, 0l, source, new SolarClientResponse(message));
		}
	}
}
