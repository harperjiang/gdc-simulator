package edu.clarkson.gdc.dashboard.service;

import java.util.Date;

import edu.clarkson.gdc.dashboard.domain.dao.HistoryDao;
import edu.clarkson.gdc.dashboard.domain.dao.NodeDao;
import edu.clarkson.gdc.dashboard.domain.dao.StatusDao;
import edu.clarkson.gdc.dashboard.domain.entity.NodeStatus;

public class DefaultInterfaceService implements InterfaceService {

	private NodeDao nodeDao;

	private StatusDao statusDao;

	private HistoryDao historyDao;

	@Override
	public void updateNodeStatus(String nodeId, String type, String value) {
		NodeStatus status = new NodeStatus();
		status.setNodeId(nodeId);
		status.setDataType(type);
		status.setValue(value);
		getStatusDao().updateStatus(status);
	}

	@Override
	public void updateNodeHistory(String nodeId, String type, String value,
			Date timestamp) {
		
	}

	public NodeDao getNodeDao() {
		return nodeDao;
	}

	public void setNodeDao(NodeDao nodeDao) {
		this.nodeDao = nodeDao;
	}

	public StatusDao getStatusDao() {
		return statusDao;
	}

	public void setStatusDao(StatusDao statusDao) {
		this.statusDao = statusDao;
	}

	public HistoryDao getHistoryDao() {
		return historyDao;
	}

	public void setHistoryDao(HistoryDao historyDao) {
		this.historyDao = historyDao;
	}

}
