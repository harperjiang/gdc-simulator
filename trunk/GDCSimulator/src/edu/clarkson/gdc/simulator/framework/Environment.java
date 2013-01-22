package edu.clarkson.gdc.simulator.framework;

import org.apache.commons.lang.Validate;

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
}
