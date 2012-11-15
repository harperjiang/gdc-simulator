package edu.clarkson.gdc.simulator.framework;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 */
public interface Stepper {

	/**
	 * 
	 */
	public void send();

	/**
	 * 
	 */
	public void process();

	/**
	 * 
	 * @return
	 */
	public Clock getClock();
}
