package edu.clarkson.gdc.dashboard.service;

import edu.clarkson.gdc.dashboard.domain.entity.VirtualMachine;

public interface VMService {

	public void migrate(String vmId, String destMachine);

	public VirtualMachine create();
}
