package edu.clarkson.gdc.dashboard.service;

import edu.clarkson.gdc.dashboard.domain.entity.VirtualMachine;
import edu.clarkson.gdc.dashboard.service.bean.ListVMResultBean;

public interface VMService {

	public static enum Operation {
		START, STOP, DESTROY, RESTART
	}

	public ListVMResultBean list(String owner);

	public void migrate(String vmName, String srcMachine, String destMachine);

	public VirtualMachine create();

	public void operate(String srcMachine, String vmName, String operation);
}
