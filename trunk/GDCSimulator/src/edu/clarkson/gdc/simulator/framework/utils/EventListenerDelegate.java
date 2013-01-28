package edu.clarkson.gdc.simulator.framework.utils;

import java.util.EventListener;

import javax.swing.event.EventListenerList;

public class EventListenerDelegate {

	private EventListenerList listenerList = new EventListenerList();

	@SuppressWarnings("unchecked")
	public <EL extends EventListener> void addListener(Class<EL> clazz,
			EL listener) {
		listenerList.add((Class<EL>) listener.getClass(), listener);
	}

	@SuppressWarnings("unchecked")
	public <EL extends EventListener> void removeListener(Class<EL> clazz,
			EL listener) {
		listenerList.remove((Class<EL>) listener.getClass(), listener);
	}

	public <EL extends EventListener> EL[] getListeners(Class<EL> listenerClass) {
		return listenerList.getListeners(listenerClass);
	}
}
