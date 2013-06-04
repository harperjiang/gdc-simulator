package edu.clarkson.gdc.dashboard.domain.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.clarkson.gdc.dashboard.domain.entity.Machine;
import edu.clarkson.gdc.dashboard.domain.entity.VirtualMachine;

public class MemoryVMDao implements VMDao {

	private Map<String, Long> lastRefresh;

	private Map<String, Map<String, VirtualMachine>> vms;

	public MemoryVMDao() {
		super();
		vms = new HashMap<String, Map<String, VirtualMachine>>();
		lastRefresh = new HashMap<String, Long>();
	}

	protected void refresh(Machine owner) {
		// TODO
	}

	@Override
	public void create(VirtualMachine vm) {
		// TODO Not implemented
	}

	@Override
	public void migrate(VirtualMachine vm, Machine source, Machine dest) {
		// TODO Auto-generated method stub

	}

	@Override
	public VirtualMachine find(Machine owner, String name) {
		refresh(owner);
		return vms.get(owner.getId()).get(name);
	}

	@Override
	public List<VirtualMachine> list(Machine owner) {
		List<VirtualMachine> result = new ArrayList<VirtualMachine>();
		result.addAll(vms.get(owner.getId()).values());
		Collections.sort(result, new Comparator<VirtualMachine>() {
			@Override
			public int compare(VirtualMachine o1, VirtualMachine o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		return result;
	}

}
