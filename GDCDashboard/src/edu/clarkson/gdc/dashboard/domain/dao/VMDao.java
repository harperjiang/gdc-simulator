package edu.clarkson.gdc.dashboard.domain.dao;

import java.util.List;

import edu.clarkson.gdc.dashboard.domain.entity.VirtualMachine;

public interface VMDao {

	VirtualMachine get(String vmId);

	List<VirtualMachine> list(String owner);

	void migrate(String vmId, String destMachine);

}
