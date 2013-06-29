package edu.clarkson.gdc.dashboard.service;

import java.math.BigDecimal;
import java.util.List;

import edu.clarkson.gdc.dashboard.domain.dao.AlertDao;
import edu.clarkson.gdc.dashboard.domain.dao.NodeDao;
import edu.clarkson.gdc.dashboard.domain.entity.Alert;
import edu.clarkson.gdc.dashboard.domain.entity.DataCenter;
import edu.clarkson.gdc.dashboard.domain.entity.Node;
import edu.clarkson.gdc.dashboard.domain.entity.Summary;

public class DefaultStructureService implements StructureService {

	private NodeDao nodeDao;

	private AlertDao alertDao;

	@Override
	public List<DataCenter> getDataCenters() {
		return nodeDao.getNodesByType(DataCenter.class);
	}

	@Override
	public List<Alert> getAlerts() {
		List<Alert> result = getAlertDao().getAlerts(null);
		for (Alert alert : result) {
			Node node = nodeDao.getNode(alert.getNodeId());
			alert.setNodeName(node.getName());
		}
		return result;
	}

	@Override
	public Summary getSystemSummary() {
		List<DataCenter> dss = getNodeDao().getNodesByType(DataCenter.class);
		Summary summary = new Summary();
		summary.setDcCount(dss.size());
		summary.setDcRunning(dss.size());
		summary.setMtbm(230);
		summary.setUtilization(new BigDecimal("79"));
		summary.setUsage(90);
		summary.setCapacity(20);
		return summary;
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