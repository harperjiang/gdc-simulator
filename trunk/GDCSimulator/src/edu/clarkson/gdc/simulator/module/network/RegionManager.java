package edu.clarkson.gdc.simulator.module.network;

import java.util.List;

public interface RegionManager {

	public Region getRegion(String id);
	
	public List<Region> getRegions();
}
