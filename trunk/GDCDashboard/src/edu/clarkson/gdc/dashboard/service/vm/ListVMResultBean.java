package edu.clarkson.gdc.dashboard.service.vm;

import java.util.List;

import edu.clarkson.gdc.dashboard.domain.entity.VirtualMachine;

public class ListVMResultBean {

	private String ownerId;

	private List<VirtualMachine> vms;

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public List<VirtualMachine> getVms() {
		return vms;
	}

	public void setVms(List<VirtualMachine> vms) {
		this.vms = vms;
	}

}
