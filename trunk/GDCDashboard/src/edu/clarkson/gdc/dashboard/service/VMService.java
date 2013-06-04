package edu.clarkson.gdc.dashboard.service;

import java.util.List;

import edu.clarkson.gdc.dashboard.domain.entity.VirtualMachine;

public interface VMService {

	public List<VirtualMachine> list(String owner);

	public void migrate(String vmId, String srcMachine, String destMachine);

	public VirtualMachine create();
}
