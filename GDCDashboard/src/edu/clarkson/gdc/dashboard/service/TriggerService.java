package edu.clarkson.gdc.dashboard.service;

import edu.clarkson.gdc.dashboard.domain.entity.NodeStatus;

public interface TriggerService {

	public void trigger(NodeStatus oldsta, NodeStatus newsta);
}
