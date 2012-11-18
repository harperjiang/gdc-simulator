package edu.clarkson.gdc.simulator.impl;

import java.util.List;

import edu.clarkson.gdc.simulator.Cloud;
import edu.clarkson.gdc.simulator.DataCenter;

/**
 * 
 * @author harper
 * 
 */
public class DefaultCloud implements Cloud {

	private static DefaultCloud instance = new DefaultCloud();

	public static DefaultCloud getInstance() {
		return instance;
	}

	private DefaultCloud() {
		super();
		setIndexService(new DefaultIndexService());
		setDataBlockDistribution(new DefaultDataDistribution());

		// Load Data Centers from Configuration

		getDataBlockDistribution().init(this);
	}

	@Override
	public List<DataCenter> getDataCenters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataCenter getNearbyDataCenter(String location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataCenter getDataCenter(String dcid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataCenter getDataCenterByLocation(String location) {
		// TODO Auto-generated method stub
		return null;
	}

	private DefaultIndexService indexService;

	private DefaultDataDistribution dataBlockDistribution;

	public DefaultIndexService getIndexService() {
		return indexService;
	}

	public void setIndexService(DefaultIndexService indexService) {
		this.indexService = indexService;
	}

	public DefaultDataDistribution getDataBlockDistribution() {
		return dataBlockDistribution;
	}

	public void setDataBlockDistribution(
			DefaultDataDistribution dataBlockDistribution) {
		this.dataBlockDistribution = dataBlockDistribution;
	}

}
