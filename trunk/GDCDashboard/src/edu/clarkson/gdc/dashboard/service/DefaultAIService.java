package edu.clarkson.gdc.dashboard.service;

import edu.clarkson.gdc.dashboard.domain.dao.HistoryDao;
import edu.clarkson.gdc.dashboard.domain.dao.NodeDao;
import edu.clarkson.gdc.dashboard.domain.dao.StatusDao;
import edu.clarkson.gdc.dashboard.domain.entity.Alert;
import edu.clarkson.gdc.dashboard.domain.entity.Machine;
import edu.clarkson.gdc.dashboard.domain.entity.NodeHistory;
import edu.clarkson.gdc.dashboard.domain.entity.NodeStatus;
import edu.clarkson.gdc.dashboard.domain.entity.StatusType;

public class DefaultAIService implements AIService {

	private NodeDao nodeDao;

	private HistoryDao historyDao;

	private StatusDao statusDao;

	private long latency = 30000;

	@Override
	public void updateStatus() {
		long current = System.currentTimeMillis();
		// Update Running Status
		for (Machine machine : nodeDao.getNodesByType(Machine.class)) {
			NodeHistory latest = historyDao.getLatest(machine, null);
			NodeStatus status = new NodeStatus();
			status.setDataType(StatusType.STATUS.name());
			if (latest == null)
				status.setValue("false");
			else
				status.setValue(Boolean.toString(current
						- latest.getTime().getTime() <= latency));
			status.setNodeId(machine.getId());
			statusDao.updateStatus(status);
		}
	}
	
	@Override
	public void handleAlert(Alert alert) {
		
		return;
	}

	@Override
	public void makeMigrationDecision() {
		// TODO Auto-generated method stub

	}

	public HistoryDao getHistoryDao() {
		return historyDao;
	}

	public void setHistoryDao(HistoryDao historyDao) {
		this.historyDao = historyDao;
	}

	public StatusDao getStatusDao() {
		return statusDao;
	}

	public void setStatusDao(StatusDao statusDao) {
		this.statusDao = statusDao;
	}

	public long getLatency() {
		return latency;
	}

	public void setLatency(long latency) {
		this.latency = latency;
	}

	public NodeDao getNodeDao() {
		return nodeDao;
	}

	public void setNodeDao(NodeDao nodeDao) {
		this.nodeDao = nodeDao;
	}

}
