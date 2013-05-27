package edu.clarkson.gdc.simulator.scenario.workload.readmywrite;

import edu.clarkson.gdc.simulator.framework.storage.DefaultCacheStorage;

public class WorkloadStorage extends DefaultCacheStorage {

	public WorkloadStorage() {
		setSize(1000);
		setReadTime(50);
		setWriteTime(55);
	}
}
