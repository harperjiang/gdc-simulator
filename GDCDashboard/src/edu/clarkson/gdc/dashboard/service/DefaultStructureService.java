package edu.clarkson.gdc.dashboard.service;

import java.math.BigDecimal;
import java.util.List;

import edu.clarkson.gdc.dashboard.domain.dao.AlertDao;
import edu.clarkson.gdc.dashboard.domain.dao.MigrationLogDao;
import edu.clarkson.gdc.dashboard.domain.dao.NodeDao;
import edu.clarkson.gdc.dashboard.domain.dao.StatusDao;
import edu.clarkson.gdc.dashboard.domain.entity.Alert;
import edu.clarkson.gdc.dashboard.domain.entity.DataCenter;
import edu.clarkson.gdc.dashboard.domain.entity.MigrationLog;
import edu.clarkson.gdc.dashboard.domain.entity.Node;
import edu.clarkson.gdc.dashboard.domain.entity.NodeStatus;
import edu.clarkson.gdc.dashboard.domain.entity.StatusType;
import edu.clarkson.gdc.dashboard.domain.entity.Summary;

public class DefaultStructureService implements StructureService {

	private NodeDao nodeDao;

	private AlertDao alertDao;

	private StatusDao statusDao;

	private MigrationLogDao migrationLogDao;

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
		Summary summary = new Summary();

		List<DataCenter> dss = getNodeDao().getNodesByType(DataCenter.class);
		summary.setDcCount(dss.size());

		summary.setGtDcCount(0);
		summary.setGpDcCount(0);

		for (DataCenter dc : dss) {
			if (dc.getPowerSource() != null) {
				summary.setGpDcCount(summary.getGpDcCount() + 1);
			} else {
				summary.setGtDcCount(summary.getGtDcCount() + 1);
			}
		}

		NodeStatus vmStatus = getStatusDao().getStatus(null,
				StatusType.SUMMARY_VMRUNNING);
		if (vmStatus != null)
			summary.setVmRunning(Integer.valueOf(vmStatus.getValue()));

		NodeStatus utilStatus = getStatusDao().getStatus(null,
				StatusType.SUMMARY_UTIL);
		if (utilStatus != null)
			summary.setUtilization(new BigDecimal(utilStatus.getValue()));

		summary.setUsage(90);
		summary.setCapacity(summary.getVmRunning());
		return summary;
	}

	@Override
	public List<MigrationLog> getMigrationLogs() {
		return getMigrationLogDao().getLatest(300000);
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

	public StatusDao getStatusDao() {
		return statusDao;
	}

	public void setStatusDao(StatusDao statusDao) {
		this.statusDao = statusDao;
	}

	public MigrationLogDao getMigrationLogDao() {
		return migrationLogDao;
	}

	public void setMigrationLogDao(MigrationLogDao migrationLogDao) {
		this.migrationLogDao = migrationLogDao;
	}

}
