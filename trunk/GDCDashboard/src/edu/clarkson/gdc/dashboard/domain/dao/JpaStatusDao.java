package edu.clarkson.gdc.dashboard.domain.dao;

import java.util.Map;

import org.springframework.orm.jpa.support.JpaDaoSupport;

import edu.clarkson.gdc.dashboard.domain.entity.Node;
import edu.clarkson.gdc.dashboard.domain.entity.NodeStatus;

public class JpaStatusDao extends JpaDaoSupport implements StatusDao {

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

}
