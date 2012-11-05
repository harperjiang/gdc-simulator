package edu.clarkson.gdc.simulator;

import java.util.List;

public interface Cloud {

	public List<DataCenter> getDataCenters();

	public DataCenter getNearbyDataCenter(String location);

	public DataCenter getDataCenter(String dcid);

	public DataCenter getDataCenterByLocation(String location);

	public IndexService getIndexService();

	public DataBlockDistribution getDataBlockDistribution();

}
