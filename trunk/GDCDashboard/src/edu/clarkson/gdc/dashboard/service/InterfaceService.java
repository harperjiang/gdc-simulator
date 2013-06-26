package edu.clarkson.gdc.dashboard.service;

import java.util.Date;

import edu.clarkson.gdc.dashboard.domain.entity.Alert;
import edu.clarkson.gdc.dashboard.domain.entity.NodeHistory;
import edu.clarkson.gdc.dashboard.domain.entity.NodeStatus;

public interface InterfaceService {

	public NodeStatus updateNodeStatus(String nodeId, String type, String value);

	public NodeHistory updateNodeHistory(String nodeId, String type, String value,
			Date timestamp);

	public Alert updateAlert(String nodeId, String type, String value);
}
