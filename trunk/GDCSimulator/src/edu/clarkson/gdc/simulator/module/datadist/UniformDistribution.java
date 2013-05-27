package edu.clarkson.gdc.simulator.module.datadist;

import java.util.ArrayList;
import java.util.List;

import edu.clarkson.gdc.simulator.DataCenter;
import edu.clarkson.gdc.simulator.framework.DataDistribution;
import edu.clarkson.gdc.simulator.framework.Environment;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 * 
 */
public class UniformDistribution implements DataDistribution {

	@Override
	public void init(Environment env, List<DataCenter> dcs) {
		// Init Key Allocation Between Data Centers
		dcids = new ArrayList<String>();
		for (DataCenter dc : dcs)
			dcids.add(dc.getId());
	}

	private List<String> dcids;

	@Override
	public List<String> locate(String key) {
		return dcids;
	}

	public List<String> choose(String key) {
		return dcids;
	}

}
