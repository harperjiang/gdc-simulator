package edu.clarkson.gdc.dashboard.service;

import java.util.Date;

import edu.clarkson.gdc.dashboard.domain.dao.AlertDao;
import edu.clarkson.gdc.dashboard.domain.dao.NodeDao;
import edu.clarkson.gdc.dashboard.domain.entity.Alert;
import edu.clarkson.gdc.dashboard.domain.entity.AlertType;
import edu.clarkson.gdc.dashboard.domain.entity.Node;
import edu.clarkson.gdc.dashboard.domain.entity.NodeStatus;
import edu.clarkson.gdc.dashboard.domain.entity.PowerSource;
import edu.clarkson.gdc.dashboard.domain.entity.StatusType;

public class DefaultTriggerService implements TriggerService {

	private NodeDao nodeDao;

	private AlertDao alertDao;

	private static final String BTY_LOW_LEVEL = "BTY_LOW_LEVEL";

	private static final String BTY_NORMAL_LEVEL = "BTY_NORMAL_LEVEL";

	private static final String BTY_HIGH_LEVEL = "BTY_HIGH_LEVEL";

	@Override
	public Alert trigger(NodeStatus oldsta, NodeStatus newsta) {
		if (null == oldsta || null == newsta)
			return null;
		Node node = nodeDao.getNode(newsta.getNodeId());
		if (node instanceof PowerSource
				&& StatusType.BTY_LEVEL.name().equals(newsta.getDataType())) {
			String oldval = oldsta.getValue();
			String newval = newsta.getValue();
			if (BTY_NORMAL_LEVEL.equals(oldval) && BTY_LOW_LEVEL.equals(newval)) {
				Alert alert = new Alert();
				alert.setType(AlertType.BTY_TOO_LOW);
				alert.setLevel(AlertType.BTY_TOO_LOW.level());
				alert.setNodeId(node.getId());
				alert.setTime(new Date());
				alert.setNodeName(node.getName());
				getAlertDao().save(alert);
				return alert;
			}
			if (BTY_NORMAL_LEVEL.equals(oldval) && BTY_HIGH_LEVEL.equals(newval)) {
				Alert alert = new Alert();
				alert.setType(AlertType.BTY_IS_HIGH);
				alert.setLevel(AlertType.BTY_IS_HIGH.level());
				alert.setNodeId(node.getId());
				alert.setTime(new Date());
				alert.setNodeName(node.getName());
				getAlertDao().save(alert);
				return alert;
			}
		}
		return null;
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
}
