package edu.clarkson.gdc.simulator.framework;


/**
 * ExceptionStrategy is introduced to simulate unexpected exception when
 * executing
 * 
 * @author Hao Jiang
 * @since GDCSimulator 1.0
 * @version 1.0
 */
public interface ExceptionStrategy {

	/**
	 * Whether the server fail to respond to message
	 * 
	 * @param tick
	 * @return
	 */
	public NodeException getException(long tick);

}
