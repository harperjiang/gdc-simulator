package edu.clarkson.gdc.simulator.demoimpl;

import edu.clarkson.gdc.simulator.Data;

public class DemoData implements Data {

	private String key;
	
	public DemoData(String key) {
		super();
		this.key = key;
	}
	
	@Override
	public String getKey() {
		return key;
	}
}
