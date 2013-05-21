package edu.clarkson.gdc.simulator.scenario.simple.message;

import java.awt.geom.Point2D;

import edu.clarkson.gdc.simulator.framework.DataMessage;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 * 
 */
public class LocateDCRequest extends DataMessage {

	private String key;

	private Point2D clientLoc;

	public LocateDCRequest(String key, Point2D clientLoc) {
		super();
		this.key = key;
		this.clientLoc = clientLoc;
	}

	public String getKey() {
		return key;
	}

	public Point2D getClientLoc() {
		return clientLoc;
	}

	public void setClientLoc(Point2D clientLoc) {
		this.clientLoc = clientLoc;
	}

}
