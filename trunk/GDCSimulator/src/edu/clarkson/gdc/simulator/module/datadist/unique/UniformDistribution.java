package edu.clarkson.gdc.simulator.module.datadist.unique;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;

import edu.clarkson.gdc.simulator.Cloud;
import edu.clarkson.gdc.simulator.DataDistribution;
import edu.clarkson.gdc.simulator.DataCenter;
import edu.clarkson.gdc.simulator.scenario.latency.simple.DefaultCloud;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 * 
 */
public class UniformDistribution implements DataDistribution {

	@Override
	public void init(Cloud cloud) {
		Validate.isTrue(cloud instanceof DefaultCloud);
		// Init Key Allocation Between Data Centers
		dcids = new ArrayList<String>();
		for (DataCenter dc : cloud.getDataCenters())
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
