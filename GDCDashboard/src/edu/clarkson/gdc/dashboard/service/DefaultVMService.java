package edu.clarkson.gdc.dashboard.service;

import java.util.List;

import edu.clarkson.gdc.dashboard.domain.dao.NodeDao;
import edu.clarkson.gdc.dashboard.domain.dao.VMDao;
import edu.clarkson.gdc.dashboard.domain.entity.VirtualMachine;

public class DefaultVMService implements VMService {

	private NodeDao nodeDao;

	private VMDao vmDao;

	@Override
	public List<VirtualMachine> list(String owner) {
		return vmDao.list(owner);
	}

	@Override
	public void migrate(String vmId, String destMachine) {
		vmDao.migrate(vmId, destMachine);
	}

	@Override
	public VirtualMachine create() {
		// TODO
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
