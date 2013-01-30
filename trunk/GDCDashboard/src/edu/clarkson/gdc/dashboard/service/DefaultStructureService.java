package edu.clarkson.gdc.dashboard.service;

import java.util.ArrayList;
import java.util.List;

import edu.clarkson.gdc.dashboard.entity.DataCenter;

public class DefaultStructureService implements StructureService {

	@Override
	public List<DataCenter> getDataCenters() {
		List<DataCenter> dcs = new ArrayList<DataCenter>();

		DataCenter dc1 = new DataCenter();
		dc1.setId("dc1");
		dc1.setName("Data Center 1");
		dcs.add(dc1);
		
		

		DataCenter dc2 = new DataCenter();
		dc2.setId("dc2");
		dc2.setName("Data Center 2");
		dcs.add(dc2);

		return dcs;
	}

}
