package edu.clarkson.gdc.simulator.impl;

import java.util.List;

import org.apache.commons.lang.Validate;

import edu.clarkson.gdc.simulator.Cloud;
import edu.clarkson.gdc.simulator.DataBlockDistribution;

public class DefaultDataDistribution implements DataBlockDistribution {

	@Override
	public void init(Cloud cloud) {
		Validate.isTrue(cloud instanceof DefaultCloud);
		// Init Key Allocation Between Data Centers

	}

	@Override
	public List<String> locate(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> choose(String key) {
		return null;
	}

}
