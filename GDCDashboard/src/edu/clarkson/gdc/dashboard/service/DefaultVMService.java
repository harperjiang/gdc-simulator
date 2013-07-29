package edu.clarkson.gdc.dashboard.service;

import edu.clarkson.gdc.dashboard.domain.dao.NodeDao;
import edu.clarkson.gdc.dashboard.domain.dao.VMDao;
import edu.clarkson.gdc.dashboard.domain.entity.Attributes;
import edu.clarkson.gdc.dashboard.domain.entity.Machine;
import edu.clarkson.gdc.dashboard.domain.entity.VMStatus;
import edu.clarkson.gdc.dashboard.domain.entity.VirtualMachine;
import edu.clarkson.gdc.dashboard.service.vm.ListVMResultBean;

public class DefaultVMService implements VMService {

	private NodeDao nodeDao;

	private VMDao vmDao;

	@Override
	public ListVMResultBean list(String owner) {
		Machine ownerMachine = getNodeDao().getNode(owner);
		ListVMResultBean bean = new ListVMResultBean();
		bean.setOwnerId(owner);
		bean.setVms(vmDao.list(ownerMachine));
		return bean;
	}

	@Override
	public void migrate(String vmId, String srcId, String destId) {
		Machine srcMachine = getNodeDao().getNode(srcId);
		Machine destMachine = getNodeDao().getNode(destId);
		VirtualMachine vm = getVmDao().find(srcMachine, vmId);
		// TODO Test whether this vm exists on source
		vmDao.migrate(vm, srcMachine, destMachine);
	}

	@Override
	public void operate(String srcId, String vmName, String operation) {
		Machine srcMachine = getNodeDao().getNode(srcId);
		VirtualMachine vm = getVmDao().find(srcMachine, vmName);
		vmDao.operate(srcMachine, vm, VMService.Operation.valueOf(operation));
	}

	@Override
	public Machine find(String vmName) {
		for (Machine machine : nodeDao.getNodesByType(Machine.class)) {
			VirtualMachine vm = vmDao.find(machine, vmName);
			if (vm != null
					&& VMStatus.RUNNING.value().equals(
							vm.getAttributes().get(
									Attributes.VM_STATUS.attrName()))) {
				return machine;
			}
		}
		return null;
	}

	@Override
	public VirtualMachine create() {
		// TODO Not implemented
		return null;
	}

	public NodeDao getNodeDao() {
		return nodeDao;
	}

	public void setNodeDao(NodeDao nodeDao) {
		this.nodeDao = nodeDao;
	}

	public VMDao getVmDao() {
		return vmDao;
	}

	public void setVmDao(VMDao vmDao) {
		this.vmDao = vmDao;
	}

	public void migrationDone(int logId) {
		getVmDao().migrationDone(logId);
	}
}
