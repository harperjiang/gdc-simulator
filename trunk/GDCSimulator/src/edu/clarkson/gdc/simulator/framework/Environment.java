package edu.clarkson.gdc.simulator.framework;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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

	private List<Component> components;

	private Map<String, Component> index;

	public Environment() {
		// Create a clock
		clock = new Clock();

		components = new ArrayList<Component>();
		index = new HashMap<String, Component>();
		// Create Proxy
		proxy = new EventListenerProxy();
	}

	public Clock getClock() {
		return clock;
	}

	public List<Component> getComponents() {
		return components;
	}

	public Component getComponent(String id) {
		return index.get(id);
	}

	public void add(Component component) {
		if (null != component.getEnvironment()) {
			component.getEnvironment().remove(component);
		}
		Validate.notNull(getClock());
		component.setEnvironment(this);
		components.add(component);
		if (StringUtils.isNotEmpty(component.getId()))
			index.put(component.getId(), component);
		getClock().register(component);
	}

	public void remove(Component component) {
		Validate.notNull(getClock());
		component.setEnvironment(null);
		components.remove(component);
		if (StringUtils.isNotEmpty(component.getId()))
			index.remove(component.getId());
		getClock().unregister(component);
	}

	public void run(long stop) {
		for (Component comp : components) {
			comp.init();
		}

		while (getClock().getCounter() < stop) {
			getClock().tick();
		}
	}

	public void run(long start, long stop) {
		for (Component comp : components) {
			comp.init();
		}
		getClock().setCounter(start);
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
