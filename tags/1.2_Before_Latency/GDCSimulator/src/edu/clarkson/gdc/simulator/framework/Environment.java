package edu.clarkson.gdc.simulator.framework;

import java.util.EventListener;

import org.apache.commons.lang.Validate;

import edu.clarkson.gdc.simulator.framework.utils.EventListenerProxy;

/**
 * 
 * 
 * @author Hao Jiang
 * @since GDCSimulator 1.0
 * @version 1.0
 * 
 */
public class Environment {

	private Clock clock;

	public Environment() {
		// Create a clock
		clock = new Clock();
		
		// Create Proxy
		proxy = new EventListenerProxy();
	}

	public Clock getClock() {
		return clock;
	}

	public void add(Component component) {
		if (null != component.getEnvironment()) {
			component.getEnvironment().remove(component);
		}
		Validate.notNull(getClock());
		component.setEnvironment(this);
		getClock().register(component);
	}

	public void remove(Component component) {
		Validate.notNull(getClock());
		component.setEnvironment(null);
		getClock().unregister(component);
	}

	public void run(long stop) {
		while (getClock().getCounter() < stop) {
			getClock().tick();
		}
	}

	protected EventListenerProxy proxy;

	public <EL extends EventListener> EL getProbe(Class<EL> clazz) {
		return proxy.getProbe(clazz);
	}

	public <EL extends EventListener> void addListener(Class<EL> clazz,
			EL listener) {
		proxy.addListener(clazz, listener);
	}

	public <EL extends EventListener> void removeListener(Class<EL> clazz,
			EL listener) {
		proxy.removeListener(clazz, listener);
	}
}
