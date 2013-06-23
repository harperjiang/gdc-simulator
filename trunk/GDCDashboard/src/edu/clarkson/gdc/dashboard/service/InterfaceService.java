package edu.clarkson.gdc.dashboard.service;

import java.util.Date;

public interface InterfaceService {

	public void updateNodeStatus(String nodeId, String type, String value);

	public void updateNodeHistory(String nodeId, String type, String value,
			Date timestamp);

	public void updateAlert(String nodeId, String type, String value);
}
