package edu.clarkson.gdc.dashboard.service;

import java.util.List;

import edu.clarkson.gdc.dashboard.domain.dao.NodeDao;
import edu.clarkson.gdc.dashboard.domain.dao.VMDao;
import edu.clarkson.gdc.dashboard.domain.entity.Machine;
import edu.clarkson.gdc.dashboard.domain.entity.VirtualMachine;

public class DefaultVMService implements VMService {

	private NodeDao nodeDao;

	private VMDao vmDao;

	@Override
	public List<VirtualMachine> list(String owner) {
		Machine ownerMachine = getNodeDao().getNode(owner);
		return vmDao.list(ownerMachine);
	}

	@Override
	public void migrate(String vmId, String srcId, String destId) {
		Machine srcMachine = getNodeDao().getNode(srcId);
		Machine destMachine = getNodeDao().getNode(destId);
		VirtualMachine vm = getVmDao().find(vmId);
		// TODO Test whether this vm exists on source
		vmDao.migrate(vm, srcMachine, destMachine);
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

}
