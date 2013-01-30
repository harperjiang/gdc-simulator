package edu.clarkson.gdc.dashboard.entity;

import java.util.ArrayList;
import java.util.List;

public class Machine extends Node {

	private List<VirtualMachine> vms;

	public Machine() {
		super();
		vms = new ArrayList<VirtualMachine>();
	}

	public List<VirtualMachine> getVms() {
		return vms;
	}
}
