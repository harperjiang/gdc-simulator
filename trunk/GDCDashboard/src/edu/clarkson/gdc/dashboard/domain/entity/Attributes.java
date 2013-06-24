package edu.clarkson.gdc.dashboard.domain.entity;

public enum Attributes {
	VM_STATUS("status"), MACHINE_IP("ip"), MACHINE_VMSIZE("vmsize");

	private String attrname;

	Attributes(String aname) {
		this.attrname = aname;
	}

	public String attrName() {
		return attrname;
	}
}
