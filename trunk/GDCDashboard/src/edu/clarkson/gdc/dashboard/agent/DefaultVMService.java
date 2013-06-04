package edu.clarkson.gdc.dashboard.agent;

import java.util.List;

import edu.clarkson.gdc.dashboard.domain.entity.VirtualMachine;
import edu.clarkson.gdc.dashboard.service.VMService;

public class DefaultVMService implements VMService {

	@Override
	public List<VirtualMachine> list(String owner) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void migrate(String vmId, String destMachine) {
		// TODO Auto-generated method stub

	}

	@Override
	public VirtualMachine create() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
