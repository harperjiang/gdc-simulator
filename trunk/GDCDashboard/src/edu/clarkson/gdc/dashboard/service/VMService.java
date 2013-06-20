package edu.clarkson.gdc.dashboard.service;

import edu.clarkson.gdc.dashboard.domain.entity.VirtualMachine;
import edu.clarkson.gdc.dashboard.service.bean.ListVMResultBean;

public interface VMService {

	public static enum Operation {
		START("start"), STOP("destroy"), RESTART("reset");

		private String operand;

		Operation(String opr) {
			this.operand = opr;
		}

		public String getOperand() {
			return operand;
		}
	}

	public ListVMResultBean list(String owner);

	public void migrate(String vmName, String srcMachine, String destMachine);

	public VirtualMachine create();

	public void operate(String srcMachine, String vmName, String operation);
}
