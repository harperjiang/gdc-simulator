package edu.clarkson.gdc.simulator;

public interface DataBlockDistribution {

	/**
	 * Initialize the distribution of blocks on cloud
	 * 
	 * @param cloud
	 */
	public void init(Cloud cloud);

	/**
	 * Determine which server(s) should this new key be placed on
	 * 
	 * @param key
	 * @return
	 */
	public void place(String key);
}
