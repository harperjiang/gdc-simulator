package edu.clarkson.gdc.simulator.framework;

import java.util.List;

import edu.clarkson.gdc.simulator.DataCenter;

public interface DataDistribution {

	/**
	 * Initialize the distribution of blocks on cloud
	 * 
	 * @param cloud
	 */
	public void init(Environment env, List<DataCenter> dcs);

	/**
	 * Determine which server(s) will this existing key be found on
	 * 
	 * @param key
	 * @return the id of data center that contains this key
	 */
	public List<String> locate(String key);

	/**
	 * Determine which server(s) should this new key be placed to
	 * 
	 * @param key
	 * @return
	 */
	public List<String> choose(String key);
}
