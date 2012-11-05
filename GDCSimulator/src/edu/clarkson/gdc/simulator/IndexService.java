package edu.clarkson.gdc.simulator;

public interface IndexService {

	/**
	 * Locate the DataCenter that contains the given key and is nearest to the
	 * client
	 * 
	 * @param key
	 * @param location
	 * @return DataCEnter Id
	 */
	public String locate(String key, String location);
}
