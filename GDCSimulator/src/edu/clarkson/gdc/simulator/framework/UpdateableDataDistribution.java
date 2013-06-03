package edu.clarkson.gdc.simulator.framework;

public interface UpdateableDataDistribution extends DataDistribution {

	void add(String key, String server);

	void remove(String key, String server);
}
