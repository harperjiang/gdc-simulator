package edu.clarkson.gdc.dashboard.domain.dao;

import java.util.HashMap;
import java.util.Map;

import edu.clarkson.gdc.dashboard.domain.entity.Node;
import edu.clarkson.gdc.dashboard.domain.entity.NodeStatus;
import edu.clarkson.gdc.dashboard.domain.entity.StatusType;

public class DummyStatusDao implements StatusDao {

	@Override
	public NodeStatus getStatus(Node node, String dataType) {
		NodeStatus ns = new NodeStatus();
		ns.setDataType(dataType);
		ns.setNodeId(node.getId());
		if (StatusType.COMMON_RUNNING.name().equals(dataType)) {
			ns.setValue("true");
		}
		return ns;
	}

	@Override
	public Map<String, NodeStatus> getStatus(Node node) {
		Map<String, NodeStatus> map = new HashMap<String, NodeStatus>();
		for (StatusType st : StatusType.values()) {
			map.put(st.key(), getStatus(node, st.name()));
		}
		return map;
	}
}
