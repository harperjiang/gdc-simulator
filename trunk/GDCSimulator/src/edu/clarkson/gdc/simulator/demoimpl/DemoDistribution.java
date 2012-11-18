package edu.clarkson.gdc.simulator.demoimpl;

import java.util.List;

import edu.clarkson.gdc.simulator.Cloud;
import edu.clarkson.gdc.simulator.DataBlockDistribution;

public class DemoDistribution implements DataBlockDistribution {

	private DemoCloud cloud;

	public DemoCloud getCloud() {
		return cloud;
	}

	public void setCloud(DemoCloud cloud) {
		this.cloud = cloud;
	}

	@Override
	public void init(Cloud cloud) {
		if(cloud instanceof DemoCloud)
			setCloud((DemoCloud)cloud);
	}

	@Override
	public List<String> locate(String key) {
		return null;
	}

	@Override
	public List<String> choose(String key) {
		// TODO Auto-generated method stub
		return null;
	}

}
