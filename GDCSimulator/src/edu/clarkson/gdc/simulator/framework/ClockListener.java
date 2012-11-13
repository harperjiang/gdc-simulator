package edu.clarkson.gdc.simulator.framework;

import java.util.EventListener;

public interface ClockListener extends EventListener {
	
	public void stepForward();
}
