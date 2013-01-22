package edu.clarkson.gdc.simulator.framework;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 */
public abstract class Component implements Stepper {

	private Environment environment;

	private long latency = 1;

	private String id;

	public Component(Environment env) {
		this.environment = env;
		env.add(this);
	}

	public Component() {

	}

	public Component(String id) {
		this();
		setId(id);
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment env) {
		if (null != getEnvironment()) {
			getEnvironment().remove(this);
		}
		this.environment = env;
	}

	public Clock getClock() {
		if (null == getEnvironment())
			return null;
		return getEnvironment().getClock();
	}

	public long getLatency() {
		return latency;
	}

	public void setLatency(long latency) {
		this.latency = latency;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
