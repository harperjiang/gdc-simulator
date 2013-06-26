package edu.clarkson.gdc.dashboard.service;

import edu.clarkson.gdc.dashboard.domain.entity.NodeStatus;

public interface ThresholdService {

	public void checkThreshold(NodeStatus status);
}
