package edu.clarkson.gdc.simulator.impl;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.clarkson.gdc.simulator.Client;
import edu.clarkson.gdc.simulator.Cloud;
import edu.clarkson.gdc.simulator.DataCenter;
import edu.clarkson.gdc.simulator.framework.Component;
import edu.clarkson.gdc.simulator.framework.Environment;
import edu.clarkson.gdc.simulator.framework.Node;
import edu.clarkson.gdc.simulator.framework.NodeMessageListener;
import edu.clarkson.gdc.simulator.framework.NodeStateListener;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.framework.utils.EventListenerProxy;
import edu.clarkson.gdc.simulator.impl.cdloader.XMLFileCloudLoader;
import edu.clarkson.gdc.simulator.impl.client.RequestIndexClient;
import edu.clarkson.gdc.simulator.impl.datadist.UniformDistribution;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 * 
 */
public class DefaultCloud extends Environment implements Cloud {

	public DefaultCloud() {
		super();
		// Init Attributes
		dataCenters = new ArrayList<DataCenter>();
		dataCenterIndex = new HashMap<String, DataCenter>();

		// Creating Event Proxy
		proxy = new EventListenerProxy();

		// Create Index Service
		DefaultIndexService dis = new DefaultIndexService();
		setIndexService(dis);
		setDataBlockDistribution(new UniformDistribution());
		setLoader(new XMLFileCloudLoader());

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

	private EventListenerProxy proxy;

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

	private UniformDistribution dataBlockDistribution;

	private CloudDataLoader loader;

	public DefaultIndexService getIndexService() {
		return indexService;
	}

	public void setIndexService(DefaultIndexService indexService) {
		this.indexService = indexService;
		add(indexService);
	}

	public UniformDistribution getDataBlockDistribution() {
		return dataBlockDistribution;
	}

	public void setDataBlockDistribution(
			UniformDistribution dataBlockDistribution) {
		this.dataBlockDistribution = dataBlockDistribution;
	}

	public CloudDataLoader getLoader() {
		return loader;
	}

	public void setLoader(CloudDataLoader loader) {
		this.loader = loader;
	}

	public void run(long stop) {
		while (getClock().getCounter() < stop) {
			getClock().tick();
		}
	}

	public <EL extends EventListener> void addListener(Class<EL> clazz,
			EL listener) {
		proxy.addListener(clazz, listener);
	}

	public <EL extends EventListener> void removeListener(Class<EL> clazz,
			EL listener) {
		proxy.removeListener(clazz, listener);
	}

}
