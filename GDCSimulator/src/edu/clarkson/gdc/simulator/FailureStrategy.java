package edu.clarkson.gdc.simulator;

/**
 * 
 * @author Hao Jiang
 * @since GDCSimulator 1.0
 * @version 1.0
 */
public interface FailureStrategy {

	/**
	 * 
	 * @param tick
	 * @return
	 */
	public boolean shouldFail(long tick);

}
