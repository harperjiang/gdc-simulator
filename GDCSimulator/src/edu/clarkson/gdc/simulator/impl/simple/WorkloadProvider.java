package edu.clarkson.gdc.simulator.impl.simple;

import java.util.List;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 * 
 */
public interface WorkloadProvider {

	/**
	 * 
	 * @param tick
	 * @return
	 */
	public List<String> fetchReadLoad(long tick);

}
