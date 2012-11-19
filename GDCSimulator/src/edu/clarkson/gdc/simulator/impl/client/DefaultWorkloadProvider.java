package edu.clarkson.gdc.simulator.impl.client;

import java.util.List;

import edu.clarkson.gdc.simulator.impl.WorkloadProvider;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 * 
 */
public class DefaultWorkloadProvider implements WorkloadProvider {

	@Override
	public List<String> fetchReadLoad(long tick) {
		return null;
	}

	public void load(String fileName, int unit) {

	}

}
