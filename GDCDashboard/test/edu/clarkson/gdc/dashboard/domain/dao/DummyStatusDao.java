package edu.clarkson.gdc.dashboard.domain.dao;

import java.util.Map;

import edu.clarkson.gdc.dashboard.domain.entity.Node;
import edu.clarkson.gdc.dashboard.domain.entity.NodeStatus;

public class DummyStatusDao implements StatusDao {

	@Override
	public NodeStatus getStatus(Node node, String dataType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, NodeStatus> getStatus(Node node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateStatus(NodeStatus status) {
		// TODO Auto-generated method stub

	}

}
