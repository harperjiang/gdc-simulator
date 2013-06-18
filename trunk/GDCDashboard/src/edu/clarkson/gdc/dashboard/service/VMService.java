package edu.clarkson.gdc.dashboard.service;

import edu.clarkson.gdc.dashboard.domain.entity.VirtualMachine;
import edu.clarkson.gdc.dashboard.service.bean.ListVMResultBean;

public interface VMService {

	public ListVMResultBean list(String owner);

	public void migrate(String vmId, String srcMachine, String destMachine);

	public VirtualMachine create();
}
