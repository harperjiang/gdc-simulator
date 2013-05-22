package edu.clarkson.gdc.simulator.scenario.latency.simple;

import java.util.List;

import edu.clarkson.gdc.simulator.Client;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 * 
 */
public interface CloudDataLoader {

	public int loadUnit();

	public List<DefaultDataCenter> loadDataCenters();

	public List<Client> loadClients();
}
