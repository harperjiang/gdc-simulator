package edu.clarkson.gdc.dashboard.domain.dao;

import java.util.Map;

import edu.clarkson.gdc.dashboard.domain.entity.Node;
import edu.clarkson.gdc.dashboard.domain.entity.NodeStatus;
import edu.clarkson.gdc.dashboard.domain.entity.StatusType;

public interface StatusDao {

	public NodeStatus getStatus(Node node, StatusType dataType);

	public Map<String, NodeStatus> getStatus(Node node);

	public void updateStatus(NodeStatus status);
}
