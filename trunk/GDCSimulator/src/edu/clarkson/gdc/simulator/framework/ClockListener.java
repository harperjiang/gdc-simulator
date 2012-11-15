package edu.clarkson.gdc.simulator.framework;

import java.util.EventListener;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 */
public interface ClockListener extends EventListener {

	/**
	 * 
	 * @param event
	 */
	public void stepForward(ClockEvent event);
}
