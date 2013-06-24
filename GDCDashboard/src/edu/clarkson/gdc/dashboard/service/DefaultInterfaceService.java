package edu.clarkson.gdc.dashboard.service;

import java.util.Date;

import edu.clarkson.gdc.dashboard.domain.dao.AlertDao;
import edu.clarkson.gdc.dashboard.domain.dao.HistoryDao;
import edu.clarkson.gdc.dashboard.domain.dao.NodeDao;
import edu.clarkson.gdc.dashboard.domain.dao.StatusDao;
import edu.clarkson.gdc.dashboard.domain.entity.Alert;
import edu.clarkson.gdc.dashboard.domain.entity.AlertType;
import edu.clarkson.gdc.dashboard.domain.entity.Node;
import edu.clarkson.gdc.dashboard.domain.entity.NodeHistory;
import edu.clarkson.gdc.dashboard.domain.entity.NodeStatus;
import edu.clarkson.gdc.workflow.WorkflowContext;

public class DefaultInterfaceService implements InterfaceService {

	private NodeDao nodeDao;

	private StatusDao statusDao;

	private HistoryDao historyDao;

	private AlertDao alertDao;

	@Override
	public void updateNodeStatus(String nodeId, String type, String value) {
		if (!NodeStatus.isStatus(type))
			return;
		NodeStatus status = new NodeStatus();
		status.setNodeId(nodeId);
		status.setDataType(type);
		status.setValue(value);
		getStatusDao().updateStatus(status);
	}

	@Override
	public void updateNodeHistory(String nodeId, String type, String value,
			Date timestamp) {
		if (!NodeHistory.isHistory(type))
			return;
		NodeHistory history = new NodeHistory();
		history.setNodeId(nodeId);
		history.setTime(timestamp);
		history.setDataType(type);
		history.setValue(value);
		getHistoryDao().addHistory(history);
	}

	@Override
	public void updateAlert(String nodeId, String type, String value) {
		if (!Alert.isAlert(type))
			return;
		Node node = getNodeDao().getNode(nodeId);

		AlertType t = AlertType.valueOf(type);

		Alert alert = new Alert();
		alert.setNodeId(nodeId);
		alert.setNodeName(node.getName());
		alert.setTime(new Date());
		alert.setType(t);
		alert.setLevel(t.level());

		getAlertDao().save(alert);

		WorkflowContext.get().getContext().put("alert", alert);
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

	public AlertDao getAlertDao() {
		return alertDao;
	}

	public void setAlertDao(AlertDao alertDao) {
		this.alertDao = alertDao;
	}

}
