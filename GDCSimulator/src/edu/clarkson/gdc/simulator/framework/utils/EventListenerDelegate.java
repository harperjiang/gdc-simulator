package edu.clarkson.gdc.simulator.framework.utils;

import java.util.EventListener;

import javax.swing.event.EventListenerList;

public class EventListenerDelegate {

	private EventListenerList listenerList = new EventListenerList();

	public <EL extends EventListener> void addListener(Class<EL> clazz,
			EL listener) {
		listenerList.add(clazz, listener);
	}

	public <EL extends EventListener> void removeListener(Class<EL> clazz,
			EL listener) {
		listenerList.remove(clazz, listener);
	}

	public <EL extends EventListener> EL[] getListeners(Class<EL> listenerClass) {
		return listenerList.getListeners(listenerClass);
	}
}
