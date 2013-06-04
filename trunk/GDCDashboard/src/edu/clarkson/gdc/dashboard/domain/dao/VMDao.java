package edu.clarkson.gdc.dashboard.domain.dao;

import java.util.List;

import edu.clarkson.gdc.dashboard.domain.entity.Machine;
import edu.clarkson.gdc.dashboard.domain.entity.VirtualMachine;

public interface VMDao {

	void create(VirtualMachine vm);

	VirtualMachine find(Machine owner, String name);

	List<VirtualMachine> list(Machine owner);

	void migrate(VirtualMachine vm, Machine source, Machine dest);

}
