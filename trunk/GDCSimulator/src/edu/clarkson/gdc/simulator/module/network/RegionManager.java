package edu.clarkson.gdc.simulator.module.network;

import java.util.List;

public interface RegionManager {

	public Region getRegion(String name);
	
	public List<Region> getRegions();
}
