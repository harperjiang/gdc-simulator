package edu.clarkson.gdc.simulator.impl;

import java.util.List;

import edu.clarkson.gdc.simulator.Client;
import edu.clarkson.gdc.simulator.DataCenter;

/**
 * 
 * @author Hao Jiang
 * @since Simulator 1.0
 * @version 1.0
 * 
 */
public interface CloudDataLoader {

	public int loadUnit();

	public List<DataCenter> loadDataCenters();

	public List<Client> loadClients();
}
