package edu.clarkson.gdc.simulator.framework;

public interface Stepper {
	
	public void send();
	
	public void process();
	
	public Clock getClock();
}
