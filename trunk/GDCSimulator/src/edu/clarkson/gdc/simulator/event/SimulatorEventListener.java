package edu.clarkson.gdc.simulator.event;

import java.util.EventListener;

public interface SimulatorEventListener extends EventListener {

	void accessBetweenNodes(SimulatorEvent event);

	void serverFailed(SimulatorEvent event);

}
