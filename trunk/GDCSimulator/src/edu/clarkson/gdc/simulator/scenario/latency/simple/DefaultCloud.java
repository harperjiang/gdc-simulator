package edu.clarkson.gdc.simulator.scenario.latency.simple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.clarkson.gdc.simulator.Client;
import edu.clarkson.gdc.simulator.Cloud;
import edu.clarkson.gdc.simulator.DataBlockDistribution;
import edu.clarkson.gdc.simulator.DataCenter;
import edu.clarkson.gdc.simulator.framework.Component;
import edu.clarkson.gdc.simulator.framework.Environment;
import edu.clarkson.gdc.simulator.framework.Node;
import edu.clarkson.gdc.simulator.framework.NodeMessageListener;
import edu.clarkson.gdc.simulator.framework.NodeStateListener;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.framework.storage.DefaultCacheStorage;
import edu.clarkson.gdc.simulator.framework.storage.WritePolicy;
import edu.clarkson.gdc.simulator.module.datadist.ch.ConsistentHashingDistribution;
import edu.clarkson.gdc.simulator.scenario.latency.simple.cdloader.XMLFileCloudLoader;
import edu.clarkson.gdc.simulator.scenario.latency.simple.client.RequestIndexClient;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 * 
 */
public class DefaultCloud extends Environment implements Cloud {

	private DefaultCacheStorage nas;

	public DefaultCloud() {
		super();

		setLoader(new XMLFileCloudLoader());

		// Init Attributes
		dataCenters = new ArrayList<DataCenter>();
		dataCenterIndex = new HashMap<String, DataCenter>();

		// Create Index Service
		DefaultIndexService dis = new DefaultIndexService();
		dis.setId("IndexService");
		setIndexService(dis);
		setDataBlockDistribution(new ConsistentHashingDistribution());

		// Create NAS storage
		nas = new DefaultCacheStorage();
		nas.setReadTime(100);
		nas.setWriteTime(200);

		// Create Probes
		NodeMessageListener messageProbe = proxy
				.getProbe(NodeMessageListener.class);
		NodeStateListener stateProbe = proxy.getProbe(NodeStateListener.class);

		// Load Data Centers
		setUnit(loader.loadUnit());

		// TODO Switch to Spring Configuration
		for (DefaultDataCenter dc : loader.loadDataCenters()) {
			add(dc);
			dc.addListener(NodeMessageListener.class, messageProbe);
			dc.addListener(NodeStateListener.class, stateProbe);
			// Create Storage for data center
			DefaultCacheStorage dcstorage = new DefaultCacheStorage();
			dcstorage.setInnerStorage(nas);
			dcstorage.setSize(1000);
			dcstorage.setWritePolicy(WritePolicy.WRITE_BACK);
			dcstorage.setWriteTime(5);
			dcstorage.setReadTime(5);
			dcstorage.setDirtyThreshold(100);
			dc.setStorage(dcstorage);

			dataCenters.add(dc);
			dataCenterIndex.put(dc.getId(), dc);
		}
		// Init Data Distribution
		getDataBlockDistribution().init(this);
		// Register Listeners

		// Create Pipes between nodes
		for (int i = 0; i < dataCenters.size(); i++) {
			for (int j = i; j < dataCenters.size(); j++) {
				DefaultDataCenter ddc1 = (DefaultDataCenter) dataCenters.get(i);
				DefaultDataCenter ddc2 = (DefaultDataCenter) dataCenters.get(j);
				new Pipe(ddc1, ddc2).setId(ddc1.getId() + "-" + ddc2.getId());
			}
		}
		// Create Pipes between clients and dcs/ is
		clients = loader.loadClients();
		for (Client client : clients) {
			if (client instanceof Component)
				add((Component) client);

			for (DataCenter dc : dataCenters) {
				new Pipe((Node) client, (Node) dc).setId(((Node) client)
						.getId() + "-" + dc.getId());
			}
			new Pipe((Node) client, (Node) getIndexService())
					.setId(((Node) client).getId() + "-IndexService");
		}

		for (Client client : clients) {
			((RequestIndexClient) client).addListener(
					NodeMessageListener.class, messageProbe);
		}
	}

	private List<DataCenter> dataCenters;

	private Map<String, DataCenter> dataCenterIndex;

	private List<Client> clients;

	private int unit;

	public int getUnit() {
		return unit;
	}

	protected void setUnit(int unit) {
		this.unit = unit;
	}

	@Override
	public List<DataCenter> getDataCenters() {
		return dataCenters;
	}

	@Override
	public DataCenter getDataCenter(String dcid) {
		return dataCenterIndex.get(dcid);
	}

	@Override
	public DataCenter getDataCenterByLocation(Object location) {
		return getDataCenter(getIndexService().locate(null, location));
	}

	private DefaultIndexService indexService;

	private DataBlockDistribution dataBlockDistribution;

	private CloudDataLoader loader;

	public DefaultIndexService getIndexService() {
		return indexService;
	}

	public void setIndexService(DefaultIndexService indexService) {
		this.indexService = indexService;
		add(indexService);
	}

	public DataBlockDistribution getDataBlockDistribution() {
		return dataBlockDistribution;
	}

	public void setDataBlockDistribution(
			DataBlockDistribution dataBlockDistribution) {
		this.dataBlockDistribution = dataBlockDistribution;
	}

	public CloudDataLoader getLoader() {
		return loader;
	}

	public void setLoader(CloudDataLoader loader) {
		this.loader = loader;
	}

}
