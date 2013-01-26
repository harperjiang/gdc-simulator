package edu.clarkson.gdc.simulator.impl.datadist;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;

import edu.clarkson.gdc.simulator.Cloud;
import edu.clarkson.gdc.simulator.DataBlockDistribution;
import edu.clarkson.gdc.simulator.DataCenter;
import edu.clarkson.gdc.simulator.impl.DefaultCloud;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 * 
 */
public class UniformDistribution implements DataBlockDistribution {

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
