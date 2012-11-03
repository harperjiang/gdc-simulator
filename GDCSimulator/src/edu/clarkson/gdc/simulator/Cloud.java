package edu.clarkson.gdc.simulator;

import java.util.List;

public interface Cloud {

	public List<DataCenter> getDataCenters();

	public IndexService getIndexService();

	public DataBlockDistribution getDataBlockDistribution();
}
