package edu.clarkson.gdc.dashboard.service;

import edu.clarkson.gdc.dashboard.domain.entity.Alert;
import edu.clarkson.gdc.dashboard.domain.entity.NodeStatus;

public interface TriggerService {

	public Alert trigger(NodeStatus oldsta, NodeStatus newsta);
}
