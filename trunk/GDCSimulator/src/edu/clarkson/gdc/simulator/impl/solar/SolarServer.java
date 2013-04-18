package edu.clarkson.gdc.simulator.impl.solar;

import java.awt.geom.Point2D;

import edu.clarkson.gdc.simulator.framework.DataMessage;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.impl.AbstractDataCenter;

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
	}

	@Override
	protected int getPower() {
		Point2D.Double loc = getLocation();
		double timediff = loc.y * 240 * 1000 / TimeConstant.UNIT;
		double realtime = getClock().getCounter() + timediff;
		double ratio = Math.cos((realtime / (28800 * 1000 / TimeConstant.UNIT))
				* Math.PI - 3 * Math.PI / 2);
		int res = (int) (basepower * ratio);
		return res >= 0 ? res : 0;
	}

	@Override
	protected void processEach(Pipe source, DataMessage message,
			MessageRecorder recorder) {
		if (message instanceof SolarClientRead) {
			recorder.record(50l, 0l, source, new SolarClientResponse(message));
		}
	}
}
