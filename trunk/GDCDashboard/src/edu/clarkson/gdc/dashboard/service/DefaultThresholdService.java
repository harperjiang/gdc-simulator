package edu.clarkson.gdc.dashboard.service;

import java.util.ArrayList;
import java.util.List;

import edu.clarkson.gdc.dashboard.domain.dao.AlertDao;
import edu.clarkson.gdc.dashboard.domain.dao.NodeDao;
import edu.clarkson.gdc.dashboard.domain.entity.Alert;
import edu.clarkson.gdc.dashboard.domain.entity.AlertType;
import edu.clarkson.gdc.dashboard.domain.entity.Node;
import edu.clarkson.gdc.dashboard.domain.entity.NodeStatus;
import edu.clarkson.gdc.dashboard.domain.entity.PowerSource;
import edu.clarkson.gdc.dashboard.domain.entity.StatusType;
import edu.clarkson.gdc.workflow.WorkflowContext;

public class DefaultThresholdService implements ThresholdService {

	private int powerThreshold = 0;

	private NodeDao nodeDao;

	private AlertDao alertDao;

	@Override
	public void checkThreshold(NodeStatus status) {
		List<Alert> alerts = WorkflowContext.get().get("alerts",
				new ArrayList<Alert>());
		Node node = nodeDao.getNode(status.getNodeId());
		if (node instanceof PowerSource
				&& StatusType.POWER_INPUT_I.name().equals(status.getDataType())) {
			Double value = Double.valueOf(status.getValue());
			if (value < getPowerThreshold()) {
				Alert alert = new Alert();
				alert.setType(AlertType.POWER_EXHAUST);
				alert.setLevel(AlertType.POWER_EXHAUST.level());
				alert.setNodeId(node.getId());
				alert.setNodeName(node.getName());
				getAlertDao().save(alert);
				alerts.add(alert);
			}
		}
	}

	public NodeDao getNodeDao() {
		return nodeDao;
	}

	public void setNodeDao(NodeDao nodeDao) {
		this.nodeDao = nodeDao;
	}

	public AlertDao getAlertDao() {
		return alertDao;
	}

	public void setAlertDao(AlertDao alertDao) {
		this.alertDao = alertDao;
	}

	public int getPowerThreshold() {
		return powerThreshold;
	}

	public void setPowerThreshold(int powerThreshold) {
		this.powerThreshold = powerThreshold;
	}

}
