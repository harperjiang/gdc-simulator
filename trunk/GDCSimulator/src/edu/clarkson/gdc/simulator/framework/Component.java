package edu.clarkson.gdc.simulator.framework;

public abstract class Component implements Stepper {

	private Clock clock;

	private long latency;

	public Component() {
		setClock(Clock.getInstance());
		Clock.getInstance().register(this);
	}

	public Clock getClock() {
		return clock;
	}

	public void setClock(Clock clock) {
		this.clock = clock;
	}

	public long getLatency() {
		return latency;
	}

	public void setLatency(long latency) {
		this.latency = latency;
	}

}
