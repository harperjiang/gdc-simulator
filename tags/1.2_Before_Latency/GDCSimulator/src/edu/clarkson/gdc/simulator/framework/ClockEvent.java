package edu.clarkson.gdc.simulator.framework;

import java.util.EventObject;

/**
 * ClockEvent is sent out when the clock goes forward
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 * @see ClockListener
 */
public class ClockEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3355266308552563226L;

	private long count;

	public ClockEvent(Clock source) {
		super(source);
		this.count = source.getCounter();
	}

	public long getCount() {
		return count;
	}

}
