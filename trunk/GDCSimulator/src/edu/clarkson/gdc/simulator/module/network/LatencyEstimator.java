package edu.clarkson.gdc.simulator.module.network;

/**
 * 
 * @author harper
 * 
 */
public interface LatencyEstimator {

	/**
	 * Estimate the network latency between two regions
	 * 
	 * @param from
	 *            From Region
	 * @param to
	 *            To Region
	 * @return milliseconds of estimated network latency
	 */
	public long estimate(Region from, Region to);

	/**
	 * Query a region by its id
	 * 
	 * @param id
	 * @return
	 */
	public Region query(String id);
}
