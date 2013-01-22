package edu.clarkson.gdc.simulator.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.EventListenerList;

import edu.clarkson.gdc.simulator.Client;
import edu.clarkson.gdc.simulator.Cloud;
import edu.clarkson.gdc.simulator.DataCenter;
import edu.clarkson.gdc.simulator.framework.Component;
import edu.clarkson.gdc.simulator.framework.Environment;
import edu.clarkson.gdc.simulator.framework.Node;
import edu.clarkson.gdc.simulator.framework.NodeListener;
import edu.clarkson.gdc.simulator.framework.NodeResponseEvent;
import edu.clarkson.gdc.simulator.framework.Pipe;
import edu.clarkson.gdc.simulator.impl.cdloader.XMLFileCloudLoader;
import edu.clarkson.gdc.simulator.impl.client.RequestIndexClient;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 * 
 */
public class DefaultCloud extends Environment implements Cloud, NodeListener {

	public DefaultCloud() {
		super();
		// Init Attributes
		listenerList = new EventListenerList();
		dataCenters = new ArrayList<DataCenter>();
		dataCenterIndex = new HashMap<String, DataCenter>();

		// Create Index Service
		DefaultIndexService dis = new DefaultIndexService();
		setIndexService(dis);
		setDataBlockDistribution(new UniformDataDistribution());
		setLoader(new XMLFileCloudLoader());

		// Load Data Centers
		setUnit(loader.loadUnit());

		// TODO Switch to Spring Configuration
		for (DataCenter dc : loader.loadDataCenters()) {
			if (dc instanceof Component) {
				add((Component) dc);
			}
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

		for (DataCenter dc : getDataCenters()) {
			((DefaultDataCenter) dc).addListener(this);
		}
		for (Client client : clients) {
			((RequestIndexClient) client).addListener(this);
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

	private UniformDataDistribution dataBlockDistribution;

	private CloudDataLoader loader;

	public DefaultIndexService getIndexService() {
		return indexService;
	}

	public void setIndexService(DefaultIndexService indexService) {
		this.indexService = indexService;
		add(indexService);
	}

	public UniformDataDistribution getDataBlockDistribution() {
		return dataBlockDistribution;
	}

	public void setDataBlockDistribution(
			UniformDataDistribution dataBlockDistribution) {
		this.dataBlockDistribution = dataBlockDistribution;
	}

	public CloudDataLoader getLoader() {
		return loader;
	}

	public void setLoader(CloudDataLoader loader) {
		this.loader = loader;
	}

	@Override
	public void successReceived(NodeResponseEvent event) {
		for (NodeListener nl : listenerList.getListeners(NodeListener.class)) {
			nl.successReceived(event);
		}
	}

	@Override
	public void failureReceived(NodeResponseEvent event) {
		for (NodeListener nl : listenerList.getListeners(NodeListener.class)) {
			nl.failureReceived(event);
		}
	}

	private EventListenerList listenerList;

	public void addNodeListener(NodeListener listener) {
		listenerList.add(NodeListener.class, listener);
	}

	public void removeNodeListener(NodeListener listener) {
		listenerList.remove(NodeListener.class, listener);
	}

	public void run(long stop) {
		while (getClock().getCounter() < stop) {
			getClock().step();
		}
	}

}
