package edu.clarkson.gdc.dashboard.service;

import java.util.ArrayList;
import java.util.List;

import edu.clarkson.gdc.dashboard.entity.Battery;
import edu.clarkson.gdc.dashboard.entity.DataCenter;
import edu.clarkson.gdc.dashboard.entity.Machine;
import edu.clarkson.gdc.dashboard.entity.VirtualMachine;

public class DefaultStructureService implements StructureService {

	@Override
	public List<DataCenter> getDataCenters() {
		List<DataCenter> dcs = new ArrayList<DataCenter>();

		DataCenter dc1 = new DataCenter();
		dc1.setId("dc1");
		dc1.setName("Data Center 1");

		Battery battery11 = new Battery();
		battery11.setName("Battery 1");
		dc1.getBatteries().add(battery11);

		Machine machine111 = new Machine();
		machine111.setName("Machine 1");
		battery11.getMachines().add(machine111);

		VirtualMachine vm1 = new VirtualMachine();
		vm1.setName("Virtual Machine 1");
		machine111.getVms().add(vm1);

		Battery battery12 = new Battery();
		battery12.setName("Battery 2");

		Machine machine121 = new Machine();
		machine121.setName("Machine X");
		battery12.getMachines().add(machine121);

		dc1.getBatteries().add(battery12);

		dcs.add(dc1);

		DataCenter dc2 = new DataCenter();
		dc2.setId("dc2");
		dc2.setName("Data Center 2");
		dcs.add(dc2);

		return dcs;
	}

}
