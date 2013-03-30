package edu.clarkson.gdc.simulator.event;

import javax.swing.event.EventListenerList;

public class SimulatorEventSupport {

	private EventListenerList list;
	
	public SimulatorEventSupport() {
		super();
		list = new EventListenerList();
	}
	
	public void addListener(SimulatorEventListener listener) {
		list.add(SimulatorEventListener.class, listener);
	}
	
	public void removeListener(SimulatorEventListener listener) {
		list.remove(SimulatorEventListener.class,listener);
	}
	
	public void fireAccessBetweenNodeEvent(SimulatorEvent event) {
		for(SimulatorEventListener listener: list.getListeners(SimulatorEventListener.class))
			listener.accessBetweenNodes(event);
	}
	
	public void fireServerFailedEvent(SimulatorEvent event) {
		for(SimulatorEventListener listener: list.getListeners(SimulatorEventListener.class))
			listener.serverFailed(event);
	}

}
