package edu.clarkson.gdc.dashboard.domain.dao;

import java.util.ArrayList;
import java.util.List;

import edu.clarkson.gdc.dashboard.domain.entity.Machine;
import edu.clarkson.gdc.dashboard.domain.entity.VirtualMachine;
import edu.clarkson.gdc.dashboard.service.VMService.Operation;

public class MemoryVMDao implements VMDao {

	public MemoryVMDao() {
		super();
		migrationHistory = new ArrayList<Object[]>();
	}
	
	@Override
	public void create(VirtualMachine vm) {
		// TODO Auto-generated method stub

	}

	@Override
	public VirtualMachine find(Machine owner, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VirtualMachine> list(Machine owner) {
		VirtualMachine vm = new VirtualMachine();
		vm.setName("Good VM");
		List<VirtualMachine> vms = new ArrayList<VirtualMachine>();
		vms.add(vm);
		return vms;
	}
	
	private List<Object[]> migrationHistory;

	@Override
	public void migrate(VirtualMachine vm, Machine source, Machine dest) {
		// TODO Auto-generated method stub

	}

	@Override
	public void operate(Machine source, VirtualMachine vm, Operation operation) {
		// TODO Auto-generated method stub

	}

}
