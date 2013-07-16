package edu.clarkson.gdc.dashboard.domain.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.clarkson.gdc.dashboard.domain.entity.Machine;
import edu.clarkson.gdc.dashboard.domain.entity.VirtualMachine;
import edu.clarkson.gdc.dashboard.service.VMService.Operation;

public class MemoryVMDao implements VMDao {

	private Map<String, Map<String, VirtualMachine>> memory;

	public MemoryVMDao() {
		super();
		memory = new HashMap<String, Map<String, VirtualMachine>>();
		migrationHistory = new ArrayList<Object[]>();
	}

	@Override
	public void create(VirtualMachine vm) {
		// TODO Auto-generated method stub

	}

	@Override
	public VirtualMachine find(Machine owner, String name) {
		return memory.get(owner.getId()).get(name);
	}

	@Override
	public List<VirtualMachine> list(Machine owner) {
		List<VirtualMachine> vms = new ArrayList<VirtualMachine>();
		vms.addAll(memory(owner.getId()).values());
		return vms;
	}

	protected Map<String, VirtualMachine> memory(String machine) {
		if (!memory.containsKey(machine)) {
			VirtualMachine vm = new VirtualMachine();
			vm.setName("Good VM from " + machine);
			Map<String, VirtualMachine> map = new HashMap<String, VirtualMachine>();
			map.put(vm.getName(), vm);
			memory.put(machine, map);
		}
		return memory.get(machine);
	}

	private List<Object[]> migrationHistory;

	@Override
	public void migrate(VirtualMachine vm, Machine source, Machine dest) {
		migrationHistory.add(new Object[] { vm, source, dest });
		memory(source.getId()).remove(vm.getName());
		memory(dest.getId()).put(vm.getName(), vm);
	}

	public List<Object[]> getMigrationHistory() {
		return migrationHistory;
	}

	@Override
	public void operate(Machine source, VirtualMachine vm, Operation operation) {

	}

}
