package edu.clarkson.gdc.dashboard.service;

import java.util.List;

import edu.clarkson.gdc.dashboard.domain.entity.Alert;
import edu.clarkson.gdc.dashboard.domain.entity.DataCenter;
import edu.clarkson.gdc.dashboard.domain.entity.MigrationLog;
import edu.clarkson.gdc.dashboard.domain.entity.Summary;

public interface StructureService {

	public List<DataCenter> getDataCenters();
	
	public List<Alert> getAlerts();
	
	public List<MigrationLog> getMigrationLogs();
	
	public Summary getSystemSummary();
}
