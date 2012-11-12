package edu.clarkson.gdc.simulator.demoimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.clarkson.gdc.simulator.Cloud;
import edu.clarkson.gdc.simulator.DataBlockDistribution;
import edu.clarkson.gdc.simulator.DataCenter;
import edu.clarkson.gdc.simulator.IndexService;
import edu.clarkson.gdc.simulator.event.SimulatorEvent;
import edu.clarkson.gdc.simulator.event.SimulatorEventListener;
import edu.clarkson.gdc.simulator.event.SimulatorEventSupport;
import edu.clarkson.gdc.simulator.event.Tuple;

public class DemoCloud implements Cloud {

	private Map<String,DataCenter> dataCenters;
	
	private DemoIndexService indexService;
	
	private DemoDistribution distribution;
	
	private SimulatorEventSupport eventSupport;
	
	public DemoCloud() {
		super();
		indexService = new DemoIndexService();
		indexService.setCloud(this);
		distribution = new DemoDistribution();
		distribution.init(this);
		
		eventSupport = new SimulatorEventSupport();
	}
	
	@Override
	public List<DataCenter> getDataCenters() {
		List<DataCenter> ds = new ArrayList<DataCenter>();
		ds.addAll(dataCenters.values());
		return ds;
	}

	@Override
	public DataCenter getNearbyDataCenter(String location) {
		return dataCenters.get(0);
	}

	@Override
	public DataCenter getDataCenter(String dcid) {
		return dataCenters.get(dcid);
	}

	@Override
	public DataCenter getDataCenterByLocation(String location) {
		return getNearbyDataCenter(location);
	}

	@Override
	public IndexService getIndexService() {
		return indexService;
	}

	@Override
	public DataBlockDistribution getDataBlockDistribution() {
		return distribution;
	}

	public void addListener(SimulatorEventListener listener) {
		eventSupport.addListener(listener);
	}

	public void removeListener(SimulatorEventListener listener) {
		eventSupport.removeListener(listener);
	}

	public void fireAccessBetweenNodeEvent(Object source, Object target) {
		SimulatorEvent event = new SimulatorEvent(new Tuple(source,target));
		eventSupport.fireAccessBetweenNodeEvent(event);
	}

	public void fireServerFailedEvent(Object server) {
		SimulatorEvent event = new SimulatorEvent(server);
		eventSupport.fireServerFailedEvent(event);
	}
	
	
}
