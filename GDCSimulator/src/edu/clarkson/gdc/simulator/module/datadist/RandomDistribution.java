package edu.clarkson.gdc.simulator.module.datadist;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.Validate;

import edu.clarkson.gdc.simulator.DataCenter;
import edu.clarkson.gdc.simulator.framework.DataDistribution;
import edu.clarkson.gdc.simulator.framework.Environment;

public class RandomDistribution implements DataDistribution {

	private List<DataCenter> dataCenters;

	@Override
	public void init(Environment env, List<DataCenter> dcs) {
		this.dataCenters = dcs;
	}

	@Override
	public List<String> locate(String key) {
		return null;
	}

	@Override
	public List<String> choose(String key) {
		Validate.isTrue(writeCopy <= dataCenters.size());
		List<String> result = new ArrayList<String>();

		List<String> dcids = new ArrayList<String>();
		for (DataCenter dc : dataCenters) {
			dcids.add(dc.getId());
		}
		Random random = new Random();
		while (result.size() < writeCopy) {
			int index = random.nextInt(dcids.size());
			result.add(dcids.remove(index));
		}
		return result;
	}

	private int writeCopy;

	public int getWriteCopy() {
		return writeCopy;
	}

	public void setWriteCopy(int writeCopy) {
		this.writeCopy = writeCopy;
	}

}
